package com.compute.rental.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.compute.rental.common.enums.ApiTokenStatus;
import com.compute.rental.common.enums.ErrorCode;
import com.compute.rental.common.enums.RentalOrderStatus;
import com.compute.rental.common.exception.BusinessException;
import com.compute.rental.common.page.PageResult;
import com.compute.rental.common.util.DateTimeUtils;
import com.compute.rental.modules.commission.entity.CommissionRecord;
import com.compute.rental.modules.commission.mapper.CommissionRecordMapper;
import com.compute.rental.modules.order.entity.ApiCredential;
import com.compute.rental.modules.order.entity.ApiDeployOrder;
import com.compute.rental.modules.order.entity.RentalOrder;
import com.compute.rental.modules.order.entity.RentalProfitRecord;
import com.compute.rental.modules.order.entity.RentalSettlementOrder;
import com.compute.rental.modules.order.mapper.ApiCredentialMapper;
import com.compute.rental.modules.order.mapper.ApiDeployOrderMapper;
import com.compute.rental.modules.order.mapper.RentalOrderMapper;
import com.compute.rental.modules.order.mapper.RentalProfitRecordMapper;
import com.compute.rental.modules.order.mapper.RentalSettlementOrderMapper;
import com.compute.rental.modules.system.entity.SysAdminLog;
import com.compute.rental.modules.system.mapper.SysAdminLogMapper;
import com.compute.rental.modules.user.entity.AppUser;
import com.compute.rental.modules.user.entity.UserTeamRelation;
import com.compute.rental.modules.user.mapper.AppUserMapper;
import com.compute.rental.modules.user.mapper.UserTeamRelationMapper;
import com.compute.rental.modules.wallet.entity.UserWallet;
import com.compute.rental.modules.wallet.entity.WalletTransaction;
import com.compute.rental.modules.wallet.mapper.UserWalletMapper;
import com.compute.rental.modules.wallet.mapper.WalletTransactionMapper;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AdminBusinessQueryService {

    private final AppUserMapper appUserMapper;
    private final UserWalletMapper userWalletMapper;
    private final WalletTransactionMapper walletTransactionMapper;
    private final RentalOrderMapper rentalOrderMapper;
    private final ApiCredentialMapper apiCredentialMapper;
    private final ApiDeployOrderMapper apiDeployOrderMapper;
    private final RentalProfitRecordMapper profitRecordMapper;
    private final RentalSettlementOrderMapper settlementOrderMapper;
    private final CommissionRecordMapper commissionRecordMapper;
    private final UserTeamRelationMapper teamRelationMapper;
    private final SysAdminLogMapper adminLogMapper;
    private final AdminLogService adminLogService;

    public AdminBusinessQueryService(
            AppUserMapper appUserMapper,
            UserWalletMapper userWalletMapper,
            WalletTransactionMapper walletTransactionMapper,
            RentalOrderMapper rentalOrderMapper,
            ApiCredentialMapper apiCredentialMapper,
            ApiDeployOrderMapper apiDeployOrderMapper,
            RentalProfitRecordMapper profitRecordMapper,
            RentalSettlementOrderMapper settlementOrderMapper,
            CommissionRecordMapper commissionRecordMapper,
            UserTeamRelationMapper teamRelationMapper,
            SysAdminLogMapper adminLogMapper,
            AdminLogService adminLogService
    ) {
        this.appUserMapper = appUserMapper;
        this.userWalletMapper = userWalletMapper;
        this.walletTransactionMapper = walletTransactionMapper;
        this.rentalOrderMapper = rentalOrderMapper;
        this.apiCredentialMapper = apiCredentialMapper;
        this.apiDeployOrderMapper = apiDeployOrderMapper;
        this.profitRecordMapper = profitRecordMapper;
        this.settlementOrderMapper = settlementOrderMapper;
        this.commissionRecordMapper = commissionRecordMapper;
        this.teamRelationMapper = teamRelationMapper;
        this.adminLogMapper = adminLogMapper;
        this.adminLogService = adminLogService;
    }

    public PageResult<Map<String, Object>> pageUsers(
            long pageNo,
            long pageSize,
            String email,
            String userId,
            Integer status,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        var page = new Page<AppUser>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<AppUser>()
                .eq(hasText(userId), AppUser::getUserId, userId)
                .like(hasText(email), AppUser::getEmail, email)
                .eq(status != null, AppUser::getStatus, status)
                .ge(startTime != null, AppUser::getCreatedAt, startTime)
                .le(endTime != null, AppUser::getCreatedAt, endTime)
                .orderByDesc(AppUser::getId);
        var result = appUserMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords().stream().map(this::userMap).toList(),
                result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Map<String, Object> getUser(Long id) {
        return userMap(requireUser(id));
    }

    @Transactional
    public Map<String, Object> disableUser(Long id, Long adminId, String ip) {
        var user = requireUser(id);
        var now = DateTimeUtils.now();
        appUserMapper.update(null, new LambdaUpdateWrapper<AppUser>()
                .eq(AppUser::getId, id)
                .set(AppUser::getStatus, 0)
                .set(AppUser::getUpdatedAt, now));
        var runningOrders = rentalOrderMapper.selectList(new LambdaQueryWrapper<RentalOrder>()
                .eq(RentalOrder::getUserId, id)
                .eq(RentalOrder::getOrderStatus, RentalOrderStatus.RUNNING.name()));
        for (var order : runningOrders) {
            rentalOrderMapper.update(null, new LambdaUpdateWrapper<RentalOrder>()
                    .eq(RentalOrder::getId, order.getId())
                    .eq(RentalOrder::getOrderStatus, RentalOrderStatus.RUNNING.name())
                    .set(RentalOrder::getOrderStatus, RentalOrderStatus.PAUSED.name())
                    .set(RentalOrder::getPausedAt, now)
                    .set(RentalOrder::getUpdatedAt, now));
            apiCredentialMapper.update(null, new LambdaUpdateWrapper<ApiCredential>()
                    .eq(ApiCredential::getRentalOrderId, order.getId())
                    .set(ApiCredential::getTokenStatus, ApiTokenStatus.PAUSED.name())
                    .set(ApiCredential::getPausedAt, now)
                    .set(ApiCredential::getUpdatedAt, now));
        }
        adminLogService.log(adminId, "BAN_USER", "app_user", id, null, "status=0",
                "Paused running orders: " + runningOrders.size(), ip);
        return getUser(id);
    }

    @Transactional
    public Map<String, Object> enableUser(Long id, Long adminId, String ip) {
        requireUser(id);
        appUserMapper.update(null, new LambdaUpdateWrapper<AppUser>()
                .eq(AppUser::getId, id)
                .set(AppUser::getStatus, 1)
                .set(AppUser::getUpdatedAt, DateTimeUtils.now()));
        adminLogService.log(adminId, "ENABLE_USER", "app_user", id, null, "status=1",
                "User enabled without auto-resuming orders", ip);
        return getUser(id);
    }

    public PageResult<UserWallet> pageWallets(long pageNo, long pageSize, Long userId, String walletNo) {
        var page = new Page<UserWallet>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<UserWallet>()
                .eq(userId != null, UserWallet::getUserId, userId)
                .eq(hasText(walletNo), UserWallet::getWalletNo, walletNo)
                .orderByDesc(UserWallet::getId);
        var result = userWalletMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public UserWallet getWalletByUser(Long userId) {
        var wallet = userWalletMapper.selectOne(new LambdaQueryWrapper<UserWallet>()
                .eq(UserWallet::getUserId, userId)
                .last("LIMIT 1"));
        if (wallet == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "钱包不存在");
        }
        return wallet;
    }

    public PageResult<WalletTransaction> pageWalletTransactions(
            long pageNo,
            long pageSize,
            Long userId,
            String walletNo,
            String txType,
            String bizType,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        Long walletId = null;
        if (hasText(walletNo)) {
            var wallet = userWalletMapper.selectOne(new LambdaQueryWrapper<UserWallet>()
                    .eq(UserWallet::getWalletNo, walletNo)
                    .last("LIMIT 1"));
            if (wallet == null) {
                return new PageResult<>(Collections.emptyList(), 0, pageNo, pageSize);
            }
            walletId = wallet.getId();
        }
        var page = new Page<WalletTransaction>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<WalletTransaction>()
                .eq(userId != null, WalletTransaction::getUserId, userId)
                .eq(walletId != null, WalletTransaction::getWalletId, walletId)
                .eq(hasText(txType), WalletTransaction::getTxType, txType)
                .eq(hasText(bizType), WalletTransaction::getBizType, bizType)
                .ge(startTime != null, WalletTransaction::getCreatedAt, startTime)
                .le(endTime != null, WalletTransaction::getCreatedAt, endTime)
                .orderByDesc(WalletTransaction::getId);
        var result = walletTransactionMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public PageResult<RentalOrder> pageRentalOrders(
            long pageNo,
            long pageSize,
            Long userId,
            String orderNo,
            String orderStatus,
            String profitStatus,
            String settlementStatus,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        var page = new Page<RentalOrder>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<RentalOrder>()
                .eq(userId != null, RentalOrder::getUserId, userId)
                .eq(hasText(orderNo), RentalOrder::getOrderNo, orderNo)
                .eq(hasText(orderStatus), RentalOrder::getOrderStatus, orderStatus)
                .eq(hasText(profitStatus), RentalOrder::getProfitStatus, profitStatus)
                .eq(hasText(settlementStatus), RentalOrder::getSettlementStatus, settlementStatus)
                .ge(startTime != null, RentalOrder::getCreatedAt, startTime)
                .le(endTime != null, RentalOrder::getCreatedAt, endTime)
                .orderByDesc(RentalOrder::getId);
        var result = rentalOrderMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Map<String, Object> getRentalOrder(String orderNo) {
        var order = rentalOrderMapper.selectOne(new LambdaQueryWrapper<RentalOrder>()
                .eq(RentalOrder::getOrderNo, orderNo)
                .last("LIMIT 1"));
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "租赁订单不存在");
        }
        var credential = apiCredentialMapper.selectOne(new LambdaQueryWrapper<ApiCredential>()
                .eq(ApiCredential::getRentalOrderId, order.getId())
                .last("LIMIT 1"));
        var user = appUserMapper.selectById(order.getUserId());
        var result = rentalOrderMap(order);
        result.put("userName", user == null ? null : user.getNickname());
        result.put("apiCredential", credential == null ? null : credentialMap(credential));
        return result;
    }

    public PageResult<Map<String, Object>> pageApiCredentials(
            long pageNo,
            long pageSize,
            Long userId,
            String credentialNo,
            String tokenStatus,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        var page = new Page<ApiCredential>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<ApiCredential>()
                .eq(userId != null, ApiCredential::getUserId, userId)
                .eq(hasText(credentialNo), ApiCredential::getCredentialNo, credentialNo)
                .eq(hasText(tokenStatus), ApiCredential::getTokenStatus, tokenStatus)
                .ge(startTime != null, ApiCredential::getCreatedAt, startTime)
                .le(endTime != null, ApiCredential::getCreatedAt, endTime)
                .orderByDesc(ApiCredential::getId);
        var result = apiCredentialMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords().stream().map(this::credentialMap).toList(),
                result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Map<String, Object> getApiCredential(String credentialNo) {
        var credential = apiCredentialMapper.selectOne(new LambdaQueryWrapper<ApiCredential>()
                .eq(ApiCredential::getCredentialNo, credentialNo)
                .last("LIMIT 1"));
        if (credential == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "API 凭证不存在");
        }
        return credentialMap(credential);
    }

    public PageResult<ApiDeployOrder> pageApiDeployOrders(
            long pageNo,
            long pageSize,
            Long userId,
            String deployNo,
            String status,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        var page = new Page<ApiDeployOrder>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<ApiDeployOrder>()
                .eq(userId != null, ApiDeployOrder::getUserId, userId)
                .eq(hasText(deployNo), ApiDeployOrder::getDeployNo, deployNo)
                .eq(hasText(status), ApiDeployOrder::getStatus, status)
                .ge(startTime != null, ApiDeployOrder::getCreatedAt, startTime)
                .le(endTime != null, ApiDeployOrder::getCreatedAt, endTime)
                .orderByDesc(ApiDeployOrder::getId);
        var result = apiDeployOrderMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public ApiDeployOrder getApiDeployOrder(String deployNo) {
        var order = apiDeployOrderMapper.selectOne(new LambdaQueryWrapper<ApiDeployOrder>()
                .eq(ApiDeployOrder::getDeployNo, deployNo)
                .last("LIMIT 1"));
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "API 部署订单不存在");
        }
        return order;
    }

    public PageResult<RentalProfitRecord> pageProfitRecords(
            long pageNo,
            long pageSize,
            Long userId,
            String orderNo,
            String status,
            LocalDate profitDate,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        var orderId = resolveOrderId(orderNo);
        if (hasText(orderNo) && orderId == null) {
            return new PageResult<>(Collections.emptyList(), 0, pageNo, pageSize);
        }
        var page = new Page<RentalProfitRecord>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<RentalProfitRecord>()
                .eq(userId != null, RentalProfitRecord::getUserId, userId)
                .eq(orderId != null, RentalProfitRecord::getRentalOrderId, orderId)
                .eq(hasText(status), RentalProfitRecord::getStatus, status)
                .eq(profitDate != null, RentalProfitRecord::getProfitDate, profitDate)
                .ge(startTime != null, RentalProfitRecord::getCreatedAt, startTime)
                .le(endTime != null, RentalProfitRecord::getCreatedAt, endTime)
                .orderByDesc(RentalProfitRecord::getId);
        var result = profitRecordMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public RentalProfitRecord getProfitRecord(String profitNo) {
        var record = profitRecordMapper.selectOne(new LambdaQueryWrapper<RentalProfitRecord>()
                .eq(RentalProfitRecord::getProfitNo, profitNo)
                .last("LIMIT 1"));
        if (record == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "收益记录不存在");
        }
        return record;
    }

    public PageResult<RentalSettlementOrder> pageSettlementOrders(
            long pageNo,
            long pageSize,
            Long userId,
            String orderNo,
            String settlementType,
            String status,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        var orderId = resolveOrderId(orderNo);
        if (hasText(orderNo) && orderId == null) {
            return new PageResult<>(Collections.emptyList(), 0, pageNo, pageSize);
        }
        var page = new Page<RentalSettlementOrder>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<RentalSettlementOrder>()
                .eq(userId != null, RentalSettlementOrder::getUserId, userId)
                .eq(orderId != null, RentalSettlementOrder::getRentalOrderId, orderId)
                .eq(hasText(settlementType), RentalSettlementOrder::getSettlementType, settlementType)
                .eq(hasText(status), RentalSettlementOrder::getStatus, status)
                .ge(startTime != null, RentalSettlementOrder::getCreatedAt, startTime)
                .le(endTime != null, RentalSettlementOrder::getCreatedAt, endTime)
                .orderByDesc(RentalSettlementOrder::getId);
        var result = settlementOrderMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public RentalSettlementOrder getSettlementOrder(String settlementNo) {
        var order = settlementOrderMapper.selectOne(new LambdaQueryWrapper<RentalSettlementOrder>()
                .eq(RentalSettlementOrder::getSettlementNo, settlementNo)
                .last("LIMIT 1"));
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "结算订单不存在");
        }
        return order;
    }

    public PageResult<CommissionRecord> pageCommissionRecords(
            long pageNo,
            long pageSize,
            Long userId,
            String orderNo,
            Integer levelNo,
            String status,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        var orderId = resolveOrderId(orderNo);
        if (hasText(orderNo) && orderId == null) {
            return new PageResult<>(Collections.emptyList(), 0, pageNo, pageSize);
        }
        var page = new Page<CommissionRecord>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<CommissionRecord>()
                .eq(userId != null, CommissionRecord::getBenefitUserId, userId)
                .eq(orderId != null, CommissionRecord::getSourceOrderId, orderId)
                .eq(levelNo != null, CommissionRecord::getLevelNo, levelNo)
                .eq(hasText(status), CommissionRecord::getStatus, status)
                .ge(startTime != null, CommissionRecord::getCreatedAt, startTime)
                .le(endTime != null, CommissionRecord::getCreatedAt, endTime)
                .orderByDesc(CommissionRecord::getId);
        var result = commissionRecordMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public CommissionRecord getCommissionRecord(String commissionNo) {
        var record = commissionRecordMapper.selectOne(new LambdaQueryWrapper<CommissionRecord>()
                .eq(CommissionRecord::getCommissionNo, commissionNo)
                .last("LIMIT 1"));
        if (record == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "佣金记录不存在");
        }
        return record;
    }

    public PageResult<UserTeamRelation> pageTeamRelations(
            long pageNo,
            long pageSize,
            Long ancestorUserId,
            Long descendantUserId,
            Integer levelDepth
    ) {
        var page = new Page<UserTeamRelation>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<UserTeamRelation>()
                .eq(ancestorUserId != null, UserTeamRelation::getAncestorUserId, ancestorUserId)
                .eq(descendantUserId != null, UserTeamRelation::getDescendantUserId, descendantUserId)
                .eq(levelDepth != null, UserTeamRelation::getLevelDepth, levelDepth)
                .orderByAsc(UserTeamRelation::getLevelDepth)
                .orderByDesc(UserTeamRelation::getId);
        var result = teamRelationMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Map<String, Object> userTeam(Long userId) {
        requireUser(userId);
        var relations = teamRelationMapper.selectList(new LambdaQueryWrapper<UserTeamRelation>()
                .eq(UserTeamRelation::getAncestorUserId, userId)
                .orderByAsc(UserTeamRelation::getLevelDepth));
        var result = new LinkedHashMap<String, Object>();
        result.put("user_id", userId);
        result.put("total_team_count", relations.size());
        result.put("direct_team_count", countDepth(relations, 1));
        result.put("level2_team_count", countDepth(relations, 2));
        result.put("level3_team_count", countDepth(relations, 3));
        result.put("deeper_team_count", relations.stream()
                .filter(relation -> relation.getLevelDepth() != null && relation.getLevelDepth() > 3)
                .count());
        result.put("relations", relations);
        return result;
    }

    public PageResult<SysAdminLog> pageLogs(
            long pageNo,
            long pageSize,
            Long adminId,
            String action,
            String bizType,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        var page = new Page<SysAdminLog>(pageNo, pageSize);
        var wrapper = new LambdaQueryWrapper<SysAdminLog>()
                .eq(adminId != null, SysAdminLog::getAdminId, adminId)
                .eq(hasText(action), SysAdminLog::getAction, action)
                .eq(hasText(bizType), SysAdminLog::getTargetTable, bizType)
                .ge(startTime != null, SysAdminLog::getCreatedAt, startTime)
                .le(endTime != null, SysAdminLog::getCreatedAt, endTime)
                .orderByDesc(SysAdminLog::getId);
        var result = adminLogMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public SysAdminLog getLog(Long id) {
        var log = adminLogMapper.selectById(id);
        if (log == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "管理员日志不存在");
        }
        return log;
    }

    private AppUser requireUser(Long id) {
        var user = appUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        return user;
    }

    private Long resolveOrderId(String orderNo) {
        if (!hasText(orderNo)) {
            return null;
        }
        var order = rentalOrderMapper.selectOne(new LambdaQueryWrapper<RentalOrder>()
                .eq(RentalOrder::getOrderNo, orderNo)
                .last("LIMIT 1"));
        return order == null ? null : order.getId();
    }

    private Map<String, Object> userMap(AppUser user) {
        var result = new LinkedHashMap<String, Object>();
        result.put("id", user.getId());
        result.put("user_id", user.getUserId());
        result.put("email", user.getEmail());
        result.put("nickname", user.getNickname());
        result.put("status", user.getStatus());
        result.put("email_verified_at", user.getEmailVerifiedAt());
        result.put("last_login_at", user.getLastLoginAt());
        result.put("created_at", user.getCreatedAt());
        result.put("updated_at", user.getUpdatedAt());
        return result;
    }

    private Map<String, Object> rentalOrderMap(RentalOrder order) {
        var result = new LinkedHashMap<String, Object>();
        for (var field : RentalOrder.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            try {
                result.put(field.getName(), field.get(order));
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Failed to read rental order field: " + field.getName(), ex);
            }
        }
        return result;
    }

    private Map<String, Object> credentialMap(ApiCredential credential) {
        var result = new LinkedHashMap<String, Object>();
        result.put("id", credential.getId());
        result.put("credential_no", credential.getCredentialNo());
        result.put("user_id", credential.getUserId());
        result.put("rental_order_id", credential.getRentalOrderId());
        result.put("api_name", credential.getApiName());
        result.put("api_base_url", credential.getApiBaseUrl());
        result.put("token_masked", credential.getTokenMasked());
        result.put("model_name_snapshot", credential.getModelNameSnapshot());
        result.put("deploy_fee_snapshot", credential.getDeployFeeSnapshot());
        result.put("token_status", credential.getTokenStatus());
        result.put("generated_at", credential.getGeneratedAt());
        result.put("activation_paid_at", credential.getActivationPaidAt());
        result.put("activated_at", credential.getActivatedAt());
        result.put("auto_pause_at", credential.getAutoPauseAt());
        result.put("paused_at", credential.getPausedAt());
        result.put("started_at", credential.getStartedAt());
        result.put("expired_at", credential.getExpiredAt());
        result.put("revoked_at", credential.getRevokedAt());
        result.put("mock_request_count", credential.getMockRequestCount());
        result.put("mock_token_display", credential.getMockTokenDisplay());
        result.put("mock_last_refresh_at", credential.getMockLastRefreshAt());
        result.put("remark", credential.getRemark());
        result.put("created_at", credential.getCreatedAt());
        result.put("updated_at", credential.getUpdatedAt());
        return result;
    }

    private long countDepth(Iterable<UserTeamRelation> relations, int depth) {
        long count = 0;
        for (var relation : relations) {
            if (Integer.valueOf(depth).equals(relation.getLevelDepth())) {
                count++;
            }
        }
        return count;
    }

    private boolean hasText(String value) {
        return StringUtils.hasText(value);
    }
}
