package com.compute.rental.modules.recharge.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.compute.rental.common.enums.CommonStatus;
import com.compute.rental.common.enums.ErrorCode;
import com.compute.rental.common.enums.RechargeOrderStatus;
import com.compute.rental.common.enums.WalletBusinessType;
import com.compute.rental.common.exception.BusinessException;
import com.compute.rental.common.page.PageResult;
import com.compute.rental.common.util.DateTimeUtils;
import com.compute.rental.common.util.MoneyUtils;
import com.compute.rental.modules.recharge.dto.AdminApproveRechargeRequest;
import com.compute.rental.modules.recharge.dto.AdminRejectRechargeRequest;
import com.compute.rental.modules.recharge.dto.CreateRechargeOrderRequest;
import com.compute.rental.modules.recharge.dto.RechargeChannelResponse;
import com.compute.rental.modules.recharge.dto.RechargeOrderQueryRequest;
import com.compute.rental.modules.recharge.dto.RechargeOrderResponse;
import com.compute.rental.modules.system.service.SysConfigDefaults;
import com.compute.rental.modules.system.service.SysConfigService;
import com.compute.rental.modules.wallet.entity.RechargeChannel;
import com.compute.rental.modules.wallet.entity.RechargeOrder;
import com.compute.rental.modules.wallet.entity.UserWallet;
import com.compute.rental.modules.wallet.mapper.RechargeChannelMapper;
import com.compute.rental.modules.wallet.mapper.RechargeOrderMapper;
import com.compute.rental.modules.wallet.mapper.UserWalletMapper;
import com.compute.rental.modules.wallet.service.WalletService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class RechargeService {

    private static final String CURRENCY_USDT = "USDT";
    private static final String APPROVE_ACTION = "APPROVE";

    private final RechargeChannelMapper rechargeChannelMapper;
    private final RechargeOrderMapper rechargeOrderMapper;
    private final UserWalletMapper userWalletMapper;
    private final SysConfigService sysConfigService;
    private final WalletService walletService;

    public RechargeService(
            RechargeChannelMapper rechargeChannelMapper,
            RechargeOrderMapper rechargeOrderMapper,
            UserWalletMapper userWalletMapper,
            SysConfigService sysConfigService,
            WalletService walletService
    ) {
        this.rechargeChannelMapper = rechargeChannelMapper;
        this.rechargeOrderMapper = rechargeOrderMapper;
        this.userWalletMapper = userWalletMapper;
        this.sysConfigService = sysConfigService;
        this.walletService = walletService;
    }

    public List<RechargeChannelResponse> listEnabledChannels() {
        return rechargeChannelMapper.selectList(new LambdaQueryWrapper<RechargeChannel>()
                        .eq(RechargeChannel::getStatus, CommonStatus.ENABLED.value())
                        .orderByAsc(RechargeChannel::getSortNo)
                        .orderByDesc(RechargeChannel::getId))
                .stream()
                .map(this::toChannelResponse)
                .toList();
    }

    @Transactional
    public RechargeOrderResponse createOrder(Long userId, CreateRechargeOrderRequest request) {
        var channel = requireEnabledChannel(request.channelId());
        var amount = MoneyUtils.requireNonNegative(request.applyAmount());
        if (amount.signum() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_AMOUNT, "Recharge amount must be greater than 0");
        }
        var minAmount = effectiveMinAmount(channel);
        if (amount.compareTo(minAmount) < 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Recharge amount is below minimum amount");
        }
        var externalTxNo = normalizeExternalTxNo(request.externalTxNo());
        ensureExternalTxNoAvailable(externalTxNo);
        var wallet = requireWallet(userId);
        var now = DateTimeUtils.now();

        var order = new RechargeOrder();
        order.setRechargeNo(generateRechargeNo());
        order.setUserId(userId);
        order.setWalletId(wallet.getId());
        order.setChannelId(channel.getId());
        order.setCurrency(CURRENCY_USDT);
        order.setChannelNameSnapshot(channel.getChannelName());
        order.setNetworkSnapshot(channel.getNetwork());
        order.setDisplayUrlSnapshot(channel.getDisplayUrl());
        order.setAccountNoSnapshot(channel.getAccountNo());
        order.setApplyAmount(amount);
        order.setExternalTxNo(externalTxNo);
        order.setPaymentProofUrl(trimToNull(request.paymentProofUrl()));
        order.setUserRemark(trimToNull(request.userRemark()));
        order.setStatus(RechargeOrderStatus.SUBMITTED.name());
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        try {
            rechargeOrderMapper.insert(order);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(ErrorCode.IDEMPOTENCY_CONFLICT, "Duplicate recharge order or external transaction number");
        }
        return toOrderResponse(order);
    }

    public PageResult<RechargeOrderResponse> pageUserOrders(Long userId, RechargeOrderQueryRequest request) {
        var page = new Page<RechargeOrder>(request.current(), request.size());
        var wrapper = baseOrderQuery()
                .eq(RechargeOrder::getUserId, userId)
                .eq(request.status() != null, RechargeOrder::getStatus,
                        request.status() == null ? null : request.status().name())
                .ge(request.startTime() != null, RechargeOrder::getCreatedAt, request.startTime())
                .le(request.endTime() != null, RechargeOrder::getCreatedAt, request.endTime())
                .orderByDesc(RechargeOrder::getId);
        var result = rechargeOrderMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords().stream().map(this::toOrderResponse).toList(),
                result.getTotal(), result.getCurrent(), result.getSize());
    }

    public RechargeOrderResponse getUserOrder(Long userId, String rechargeNo) {
        return toOrderResponse(requireUserOrder(userId, rechargeNo));
    }

    @Transactional
    public void cancelUserOrder(Long userId, String rechargeNo) {
        var order = requireUserOrder(userId, rechargeNo);
        if (!RechargeOrderStatus.SUBMITTED.name().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "Only submitted recharge orders can be canceled");
        }
        var updated = rechargeOrderMapper.update(null, new LambdaUpdateWrapper<RechargeOrder>()
                .eq(RechargeOrder::getId, order.getId())
                .eq(RechargeOrder::getStatus, RechargeOrderStatus.SUBMITTED.name())
                .set(RechargeOrder::getStatus, RechargeOrderStatus.CANCELED.name())
                .set(RechargeOrder::getUpdatedAt, DateTimeUtils.now()));
        if (updated == 0) {
            throw new BusinessException(ErrorCode.CONCURRENT_UPDATE_FAILED, "Recharge order status changed");
        }
    }

    public PageResult<RechargeOrderResponse> pageAdminOrders(RechargeOrderQueryRequest request) {
        var page = new Page<RechargeOrder>(request.current(), request.size());
        var wrapper = baseOrderQuery()
                .eq(request.status() != null, RechargeOrder::getStatus,
                        request.status() == null ? null : request.status().name())
                .ge(request.startTime() != null, RechargeOrder::getCreatedAt, request.startTime())
                .le(request.endTime() != null, RechargeOrder::getCreatedAt, request.endTime())
                .orderByDesc(RechargeOrder::getId);
        var result = rechargeOrderMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords().stream().map(this::toOrderResponse).toList(),
                result.getTotal(), result.getCurrent(), result.getSize());
    }

    public RechargeOrderResponse getAdminOrder(String rechargeNo) {
        return toOrderResponse(requireOrder(rechargeNo));
    }

    @Transactional
    public RechargeOrderResponse approve(String rechargeNo, Long reviewedBy, AdminApproveRechargeRequest request) {
        var order = requireOrder(rechargeNo);
        if (!RechargeOrderStatus.SUBMITTED.name().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "Only submitted recharge orders can be approved");
        }
        var actualAmount = MoneyUtils.requireNonNegative(request.actualAmount());
        if (actualAmount.signum() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_AMOUNT, "Actual amount must be greater than 0");
        }
        var now = DateTimeUtils.now();
        var updated = rechargeOrderMapper.update(null, new LambdaUpdateWrapper<RechargeOrder>()
                .eq(RechargeOrder::getId, order.getId())
                .eq(RechargeOrder::getStatus, RechargeOrderStatus.SUBMITTED.name())
                .set(RechargeOrder::getStatus, RechargeOrderStatus.APPROVED.name())
                .set(RechargeOrder::getActualAmount, actualAmount)
                .set(RechargeOrder::getReviewedBy, reviewedBy)
                .set(RechargeOrder::getReviewedAt, now)
                .set(RechargeOrder::getReviewRemark, trimToNull(request.reviewRemark()))
                .set(RechargeOrder::getCreditedAt, now)
                .set(RechargeOrder::getUpdatedAt, now));
        if (updated == 0) {
            throw new BusinessException(ErrorCode.CONCURRENT_UPDATE_FAILED, "Recharge order status changed");
        }

        var tx = walletService.credit(
                order.getUserId(),
                actualAmount,
                WalletBusinessType.RECHARGE,
                order.getRechargeNo(),
                APPROVE_ACTION,
                "Recharge approved"
        );
        rechargeOrderMapper.update(null, new LambdaUpdateWrapper<RechargeOrder>()
                .eq(RechargeOrder::getId, order.getId())
                .set(RechargeOrder::getWalletTxNo, tx.getTxNo())
                .set(RechargeOrder::getUpdatedAt, DateTimeUtils.now()));

        return getAdminOrder(rechargeNo);
    }

    @Transactional
    public RechargeOrderResponse reject(String rechargeNo, Long reviewedBy, AdminRejectRechargeRequest request) {
        var order = requireOrder(rechargeNo);
        if (!RechargeOrderStatus.SUBMITTED.name().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "Only submitted recharge orders can be rejected");
        }
        var now = DateTimeUtils.now();
        var updated = rechargeOrderMapper.update(null, new LambdaUpdateWrapper<RechargeOrder>()
                .eq(RechargeOrder::getId, order.getId())
                .eq(RechargeOrder::getStatus, RechargeOrderStatus.SUBMITTED.name())
                .set(RechargeOrder::getStatus, RechargeOrderStatus.REJECTED.name())
                .set(RechargeOrder::getReviewedBy, reviewedBy)
                .set(RechargeOrder::getReviewedAt, now)
                .set(RechargeOrder::getReviewRemark, trimToNull(request.reviewRemark()))
                .set(RechargeOrder::getUpdatedAt, now));
        if (updated == 0) {
            throw new BusinessException(ErrorCode.CONCURRENT_UPDATE_FAILED, "Recharge order status changed");
        }
        return getAdminOrder(rechargeNo);
    }

    private RechargeChannel requireEnabledChannel(Long channelId) {
        var channel = rechargeChannelMapper.selectById(channelId);
        if (channel == null || !Integer.valueOf(CommonStatus.ENABLED.value()).equals(channel.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Recharge channel is unavailable");
        }
        return channel;
    }

    private UserWallet requireWallet(Long userId) {
        var wallet = userWalletMapper.selectOne(new LambdaQueryWrapper<UserWallet>()
                .eq(UserWallet::getUserId, userId)
                .last("LIMIT 1"));
        if (wallet == null) {
            throw new BusinessException(ErrorCode.WALLET_NOT_FOUND);
        }
        if (!Integer.valueOf(CommonStatus.ENABLED.value()).equals(wallet.getStatus())) {
            throw new BusinessException(ErrorCode.WALLET_DISABLED);
        }
        return wallet;
    }

    private BigDecimal effectiveMinAmount(RechargeChannel channel) {
        var globalMin = sysConfigService.getBigDecimal(SysConfigDefaults.RECHARGE_MIN_AMOUNT, new BigDecimal("500"));
        if (channel.getMinAmount() == null) {
            return globalMin;
        }
        return globalMin.max(channel.getMinAmount());
    }

    private void ensureExternalTxNoAvailable(String externalTxNo) {
        if (!StringUtils.hasText(externalTxNo)) {
            return;
        }
        var existing = rechargeOrderMapper.selectOne(new LambdaQueryWrapper<RechargeOrder>()
                .eq(RechargeOrder::getExternalTxNo, externalTxNo)
                .last("LIMIT 1"));
        if (existing != null) {
            throw new BusinessException(ErrorCode.IDEMPOTENCY_CONFLICT, "Duplicate external transaction number");
        }
    }

    private RechargeOrder requireUserOrder(Long userId, String rechargeNo) {
        var order = rechargeOrderMapper.selectOne(baseOrderQuery()
                .eq(RechargeOrder::getUserId, userId)
                .eq(RechargeOrder::getRechargeNo, rechargeNo)
                .last("LIMIT 1"));
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Recharge order not found");
        }
        return order;
    }

    private RechargeOrder requireOrder(String rechargeNo) {
        var order = rechargeOrderMapper.selectOne(baseOrderQuery()
                .eq(RechargeOrder::getRechargeNo, rechargeNo)
                .last("LIMIT 1"));
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Recharge order not found");
        }
        return order;
    }

    private LambdaQueryWrapper<RechargeOrder> baseOrderQuery() {
        return new LambdaQueryWrapper<>();
    }

    private RechargeChannelResponse toChannelResponse(RechargeChannel channel) {
        return new RechargeChannelResponse(
                channel.getId(),
                channel.getChannelCode(),
                channel.getChannelName(),
                channel.getNetwork(),
                channel.getDisplayUrl(),
                channel.getAccountName(),
                channel.getAccountNo(),
                channel.getMinAmount(),
                channel.getMaxAmount()
        );
    }

    private RechargeOrderResponse toOrderResponse(RechargeOrder order) {
        return new RechargeOrderResponse(
                order.getRechargeNo(),
                order.getChannelId(),
                order.getCurrency(),
                order.getChannelNameSnapshot(),
                order.getNetworkSnapshot(),
                order.getDisplayUrlSnapshot(),
                order.getAccountNoSnapshot(),
                order.getApplyAmount(),
                order.getActualAmount(),
                order.getExternalTxNo(),
                order.getPaymentProofUrl(),
                order.getUserRemark(),
                order.getStatus(),
                order.getReviewedBy(),
                order.getReviewedAt(),
                order.getReviewRemark(),
                order.getCreditedAt(),
                order.getWalletTxNo(),
                order.getCreatedAt()
        );
    }

    private String normalizeExternalTxNo(String externalTxNo) {
        return trimToNull(externalTxNo);
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String generateRechargeNo() {
        return "RC" + DateTimeUtils.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase(Locale.ROOT);
    }
}
