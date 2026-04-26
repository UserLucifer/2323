package com.compute.rental.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.compute.rental.common.enums.BlogPublishStatus;
import com.compute.rental.common.enums.CommonStatus;
import com.compute.rental.common.enums.ProfitStatus;
import com.compute.rental.common.enums.RechargeOrderStatus;
import com.compute.rental.common.enums.RecordSettleStatus;
import com.compute.rental.common.enums.RentalOrderStatus;
import com.compute.rental.common.enums.WithdrawOrderStatus;
import com.compute.rental.common.util.DateTimeUtils;
import com.compute.rental.common.util.MoneyUtils;
import com.compute.rental.modules.commission.entity.CommissionRecord;
import com.compute.rental.modules.commission.mapper.CommissionRecordMapper;
import com.compute.rental.modules.order.entity.RentalOrder;
import com.compute.rental.modules.order.entity.RentalProfitRecord;
import com.compute.rental.modules.order.mapper.RentalOrderMapper;
import com.compute.rental.modules.order.mapper.RentalProfitRecordMapper;
import com.compute.rental.modules.user.entity.AppUser;
import com.compute.rental.modules.user.entity.UserReferralRelation;
import com.compute.rental.modules.user.mapper.AppUserMapper;
import com.compute.rental.modules.user.mapper.UserReferralRelationMapper;
import com.compute.rental.modules.wallet.entity.RechargeOrder;
import com.compute.rental.modules.wallet.entity.UserWallet;
import com.compute.rental.modules.wallet.entity.WithdrawOrder;
import com.compute.rental.modules.wallet.mapper.RechargeOrderMapper;
import com.compute.rental.modules.wallet.mapper.UserWalletMapper;
import com.compute.rental.modules.wallet.mapper.WithdrawOrderMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final AppUserMapper appUserMapper;
    private final UserReferralRelationMapper referralRelationMapper;
    private final UserWalletMapper userWalletMapper;
    private final RechargeOrderMapper rechargeOrderMapper;
    private final WithdrawOrderMapper withdrawOrderMapper;
    private final RentalOrderMapper rentalOrderMapper;
    private final RentalProfitRecordMapper profitRecordMapper;
    private final CommissionRecordMapper commissionRecordMapper;

    public DashboardService(
            AppUserMapper appUserMapper,
            UserReferralRelationMapper referralRelationMapper,
            UserWalletMapper userWalletMapper,
            RechargeOrderMapper rechargeOrderMapper,
            WithdrawOrderMapper withdrawOrderMapper,
            RentalOrderMapper rentalOrderMapper,
            RentalProfitRecordMapper profitRecordMapper,
            CommissionRecordMapper commissionRecordMapper
    ) {
        this.appUserMapper = appUserMapper;
        this.referralRelationMapper = referralRelationMapper;
        this.userWalletMapper = userWalletMapper;
        this.rechargeOrderMapper = rechargeOrderMapper;
        this.withdrawOrderMapper = withdrawOrderMapper;
        this.rentalOrderMapper = rentalOrderMapper;
        this.profitRecordMapper = profitRecordMapper;
        this.commissionRecordMapper = commissionRecordMapper;
    }

    public Map<String, Object> overview() {
        var result = new LinkedHashMap<String, Object>();
        result.put("total_users", appUserMapper.selectCount(null));
        result.put("active_users", countUsers(CommonStatus.ENABLED.value()));
        result.put("disabled_users", countUsers(CommonStatus.DISABLED.value()));
        result.put("total_recharge_amount", totalRechargeAmount());
        result.put("total_withdraw_amount", totalWithdrawAmount());
        result.put("total_order_amount", totalOrderAmount());
        result.put("total_profit_amount", totalProfitAmount());
        result.put("total_commission_amount", totalCommissionAmount());
        result.put("running_order_count", countOrdersByStatus(RentalOrderStatus.RUNNING.name()));
        result.put("pending_recharge_count", countRechargeByStatus(RechargeOrderStatus.SUBMITTED.name()));
        result.put("pending_withdraw_count", countWithdrawByStatus(WithdrawOrderStatus.PENDING_REVIEW.name()));
        return result;
    }

    public Map<String, Object> finance() {
        var today = DateTimeUtils.today();
        var result = new LinkedHashMap<String, Object>();
        result.put("today_recharge_amount", todayRechargeAmount(today));
        result.put("today_withdraw_amount", todayWithdrawAmount(today));
        result.put("today_profit_amount", todayProfitAmount(today));
        result.put("today_commission_amount", todayCommissionAmount(today));
        result.put("wallet_total_available_balance", walletAvailableBalance());
        result.put("wallet_total_frozen_balance", walletFrozenBalance());
        return result;
    }

    public Map<String, Object> orders() {
        var todayRange = todayRange();
        var result = new LinkedHashMap<String, Object>();
        result.put("order_status_counts", orderStatusCounts());
        result.put("profit_status_counts", profitStatusCounts());
        result.put("today_new_order_count", rentalOrderMapper.selectCount(new LambdaQueryWrapper<RentalOrder>()
                .ge(RentalOrder::getCreatedAt, todayRange.start())
                .le(RentalOrder::getCreatedAt, todayRange.end())));
        result.put("today_paid_order_count", rentalOrderMapper.selectCount(new LambdaQueryWrapper<RentalOrder>()
                .ge(RentalOrder::getPaidAt, todayRange.start())
                .le(RentalOrder::getPaidAt, todayRange.end())));
        result.put("running_order_count", countOrdersByStatus(RentalOrderStatus.RUNNING.name()));
        result.put("paused_order_count", countOrdersByStatus(RentalOrderStatus.PAUSED.name()));
        return result;
    }

    public Map<String, Object> users() {
        var todayRange = todayRange();
        var monthStart = DateTimeUtils.today().withDayOfMonth(1).atStartOfDay();
        var result = new LinkedHashMap<String, Object>();
        result.put("today_new_users", appUserMapper.selectCount(new LambdaQueryWrapper<AppUser>()
                .ge(AppUser::getCreatedAt, todayRange.start())
                .le(AppUser::getCreatedAt, todayRange.end())));
        result.put("current_month_new_users", appUserMapper.selectCount(new LambdaQueryWrapper<AppUser>()
                .ge(AppUser::getCreatedAt, monthStart)
                .le(AppUser::getCreatedAt, todayRange.end())));
        result.put("active_users", countUsers(CommonStatus.ENABLED.value()));
        result.put("disabled_users", countUsers(CommonStatus.DISABLED.value()));
        result.put("users_with_parent", referralRelationMapper.selectCount(new LambdaQueryWrapper<UserReferralRelation>()
                .isNotNull(UserReferralRelation::getParentUserId)));
        result.put("users_without_parent", referralRelationMapper.selectCount(new LambdaQueryWrapper<UserReferralRelation>()
                .isNull(UserReferralRelation::getParentUserId)));
        return result;
    }

    private Long countUsers(Integer status) {
        return appUserMapper.selectCount(new LambdaQueryWrapper<AppUser>().eq(AppUser::getStatus, status));
    }

    private Long countOrdersByStatus(String status) {
        return rentalOrderMapper.selectCount(new LambdaQueryWrapper<RentalOrder>().eq(RentalOrder::getOrderStatus, status));
    }

    private Long countRechargeByStatus(String status) {
        return rechargeOrderMapper.selectCount(new LambdaQueryWrapper<RechargeOrder>().eq(RechargeOrder::getStatus, status));
    }

    private Long countWithdrawByStatus(String status) {
        return withdrawOrderMapper.selectCount(new LambdaQueryWrapper<WithdrawOrder>().eq(WithdrawOrder::getStatus, status));
    }

    private BigDecimal totalRechargeAmount() {
        return sumRecharge(rechargeOrderMapper.selectList(new LambdaQueryWrapper<RechargeOrder>()
                .eq(RechargeOrder::getStatus, RechargeOrderStatus.APPROVED.name())));
    }

    private BigDecimal totalWithdrawAmount() {
        return sumWithdraw(withdrawOrderMapper.selectList(new LambdaQueryWrapper<WithdrawOrder>()
                .eq(WithdrawOrder::getStatus, WithdrawOrderStatus.PAID.name())));
    }

    private BigDecimal totalOrderAmount() {
        return MoneyUtils.scale(rentalOrderMapper.selectList(new LambdaQueryWrapper<RentalOrder>()
                        .isNotNull(RentalOrder::getPaidAt))
                .stream()
                .map(RentalOrder::getPaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal totalProfitAmount() {
        return MoneyUtils.scale(profitRecordMapper.selectList(new LambdaQueryWrapper<RentalProfitRecord>()
                        .eq(RentalProfitRecord::getStatus, RecordSettleStatus.SETTLED.name()))
                .stream()
                .map(RentalProfitRecord::getFinalProfitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal totalCommissionAmount() {
        return MoneyUtils.scale(commissionRecordMapper.selectList(new LambdaQueryWrapper<CommissionRecord>()
                        .eq(CommissionRecord::getStatus, RecordSettleStatus.SETTLED.name()))
                .stream()
                .map(CommissionRecord::getCommissionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal todayRechargeAmount(LocalDate today) {
        return sumRecharge(rechargeOrderMapper.selectList(new LambdaQueryWrapper<RechargeOrder>()
                .eq(RechargeOrder::getStatus, RechargeOrderStatus.APPROVED.name())
                .ge(RechargeOrder::getCreditedAt, today.atStartOfDay())
                .le(RechargeOrder::getCreditedAt, today.atTime(LocalTime.MAX))));
    }

    private BigDecimal todayWithdrawAmount(LocalDate today) {
        return sumWithdraw(withdrawOrderMapper.selectList(new LambdaQueryWrapper<WithdrawOrder>()
                .eq(WithdrawOrder::getStatus, WithdrawOrderStatus.PAID.name())
                .ge(WithdrawOrder::getPaidAt, today.atStartOfDay())
                .le(WithdrawOrder::getPaidAt, today.atTime(LocalTime.MAX))));
    }

    private BigDecimal todayProfitAmount(LocalDate today) {
        return MoneyUtils.scale(profitRecordMapper.selectList(new LambdaQueryWrapper<RentalProfitRecord>()
                        .eq(RentalProfitRecord::getStatus, RecordSettleStatus.SETTLED.name())
                        .eq(RentalProfitRecord::getProfitDate, today))
                .stream()
                .map(RentalProfitRecord::getFinalProfitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal todayCommissionAmount(LocalDate today) {
        return MoneyUtils.scale(commissionRecordMapper.selectList(new LambdaQueryWrapper<CommissionRecord>()
                        .eq(CommissionRecord::getStatus, RecordSettleStatus.SETTLED.name())
                        .ge(CommissionRecord::getSettledAt, today.atStartOfDay())
                        .le(CommissionRecord::getSettledAt, today.atTime(LocalTime.MAX)))
                .stream()
                .map(CommissionRecord::getCommissionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal walletAvailableBalance() {
        return MoneyUtils.scale(userWalletMapper.selectList(new LambdaQueryWrapper<UserWallet>())
                .stream()
                .map(UserWallet::getAvailableBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal walletFrozenBalance() {
        return MoneyUtils.scale(userWalletMapper.selectList(new LambdaQueryWrapper<UserWallet>())
                .stream()
                .map(UserWallet::getFrozenBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal sumRecharge(Iterable<RechargeOrder> orders) {
        var sum = BigDecimal.ZERO;
        for (var order : orders) {
            if (order.getActualAmount() != null) {
                sum = sum.add(order.getActualAmount());
            }
        }
        return MoneyUtils.scale(sum);
    }

    private BigDecimal sumWithdraw(Iterable<WithdrawOrder> orders) {
        var sum = BigDecimal.ZERO;
        for (var order : orders) {
            if (order.getActualAmount() != null) {
                sum = sum.add(order.getActualAmount());
            }
        }
        return MoneyUtils.scale(sum);
    }

    private Map<String, Long> orderStatusCounts() {
        var result = new LinkedHashMap<String, Long>();
        for (var status : RentalOrderStatus.values()) {
            result.put(status.name(), countOrdersByStatus(status.name()));
        }
        return result;
    }

    private Map<String, Long> profitStatusCounts() {
        var result = new LinkedHashMap<String, Long>();
        for (var status : ProfitStatus.values()) {
            result.put(status.name(), rentalOrderMapper.selectCount(new LambdaQueryWrapper<RentalOrder>()
                    .eq(RentalOrder::getProfitStatus, status.name())));
        }
        return result;
    }

    private DateRange todayRange() {
        var today = DateTimeUtils.today();
        return new DateRange(today.atStartOfDay(), today.atTime(LocalTime.MAX));
    }

    private record DateRange(LocalDateTime start, LocalDateTime end) {
    }
}
