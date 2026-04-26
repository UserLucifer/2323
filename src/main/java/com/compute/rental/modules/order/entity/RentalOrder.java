package com.compute.rental.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("rental_order")
public class RentalOrder {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_no")
    private String orderNo;

    @TableField("user_id")
    private Long userId;

    @TableField("product_id")
    private Long productId;

    @TableField("ai_model_id")
    private Long aiModelId;

    @TableField("cycle_rule_id")
    private Long cycleRuleId;

    @TableField("product_code_snapshot")
    private String productCodeSnapshot;

    @TableField("product_name_snapshot")
    private String productNameSnapshot;

    @TableField("machine_code_snapshot")
    private String machineCodeSnapshot;

    @TableField("machine_alias_snapshot")
    private String machineAliasSnapshot;

    @TableField("region_name_snapshot")
    private String regionNameSnapshot;

    @TableField("gpu_model_snapshot")
    private String gpuModelSnapshot;

    @TableField("gpu_memory_snapshot_gb")
    private Integer gpuMemorySnapshotGb;

    @TableField("gpu_power_tops_snapshot")
    private BigDecimal gpuPowerTopsSnapshot;

    @TableField("gpu_rent_price_snapshot")
    private BigDecimal gpuRentPriceSnapshot;

    @TableField("token_output_per_day_snapshot")
    private Long tokenOutputPerDaySnapshot;

    @TableField("ai_model_name_snapshot")
    private String aiModelNameSnapshot;

    @TableField("ai_vendor_name_snapshot")
    private String aiVendorNameSnapshot;

    @TableField("monthly_token_consumption_snapshot")
    private BigDecimal monthlyTokenConsumptionSnapshot;

    @TableField("token_unit_price_snapshot")
    private BigDecimal tokenUnitPriceSnapshot;

    @TableField("deploy_fee_snapshot")
    private BigDecimal deployFeeSnapshot;

    @TableField("cycle_days_snapshot")
    private Integer cycleDaysSnapshot;

    @TableField("yield_multiplier_snapshot")
    private BigDecimal yieldMultiplierSnapshot;

    @TableField("early_penalty_rate_snapshot")
    private BigDecimal earlyPenaltyRateSnapshot;

    @TableField("currency")
    private String currency;

    @TableField("order_amount")
    private BigDecimal orderAmount;

    @TableField("paid_amount")
    private BigDecimal paidAmount;

    @TableField("expected_daily_profit")
    private BigDecimal expectedDailyProfit;

    @TableField("expected_total_profit")
    private BigDecimal expectedTotalProfit;

    @TableField("order_status")
    private String orderStatus;

    @TableField("profit_status")
    private String profitStatus;

    @TableField("settlement_status")
    private String settlementStatus;

    @TableField("machine_pay_tx_no")
    private String machinePayTxNo;

    @TableField("paid_at")
    private LocalDateTime paidAt;

    @TableField("api_generated_at")
    private LocalDateTime apiGeneratedAt;

    @TableField("deploy_fee_paid_at")
    private LocalDateTime deployFeePaidAt;

    @TableField("activated_at")
    private LocalDateTime activatedAt;

    @TableField("auto_pause_at")
    private LocalDateTime autoPauseAt;

    @TableField("paused_at")
    private LocalDateTime pausedAt;

    @TableField("started_at")
    private LocalDateTime startedAt;

    @TableField("profit_start_at")
    private LocalDateTime profitStartAt;

    @TableField("profit_end_at")
    private LocalDateTime profitEndAt;

    @TableField("expired_at")
    private LocalDateTime expiredAt;

    @TableField("canceled_at")
    private LocalDateTime canceledAt;

    @TableField("finished_at")
    private LocalDateTime finishedAt;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getAiModelId() {
        return aiModelId;
    }

    public void setAiModelId(Long aiModelId) {
        this.aiModelId = aiModelId;
    }

    public Long getCycleRuleId() {
        return cycleRuleId;
    }

    public void setCycleRuleId(Long cycleRuleId) {
        this.cycleRuleId = cycleRuleId;
    }

    public String getProductCodeSnapshot() {
        return productCodeSnapshot;
    }

    public void setProductCodeSnapshot(String productCodeSnapshot) {
        this.productCodeSnapshot = productCodeSnapshot;
    }

    public String getProductNameSnapshot() {
        return productNameSnapshot;
    }

    public void setProductNameSnapshot(String productNameSnapshot) {
        this.productNameSnapshot = productNameSnapshot;
    }

    public String getMachineCodeSnapshot() {
        return machineCodeSnapshot;
    }

    public void setMachineCodeSnapshot(String machineCodeSnapshot) {
        this.machineCodeSnapshot = machineCodeSnapshot;
    }

    public String getMachineAliasSnapshot() {
        return machineAliasSnapshot;
    }

    public void setMachineAliasSnapshot(String machineAliasSnapshot) {
        this.machineAliasSnapshot = machineAliasSnapshot;
    }

    public String getRegionNameSnapshot() {
        return regionNameSnapshot;
    }

    public void setRegionNameSnapshot(String regionNameSnapshot) {
        this.regionNameSnapshot = regionNameSnapshot;
    }

    public String getGpuModelSnapshot() {
        return gpuModelSnapshot;
    }

    public void setGpuModelSnapshot(String gpuModelSnapshot) {
        this.gpuModelSnapshot = gpuModelSnapshot;
    }

    public Integer getGpuMemorySnapshotGb() {
        return gpuMemorySnapshotGb;
    }

    public void setGpuMemorySnapshotGb(Integer gpuMemorySnapshotGb) {
        this.gpuMemorySnapshotGb = gpuMemorySnapshotGb;
    }

    public BigDecimal getGpuPowerTopsSnapshot() {
        return gpuPowerTopsSnapshot;
    }

    public void setGpuPowerTopsSnapshot(BigDecimal gpuPowerTopsSnapshot) {
        this.gpuPowerTopsSnapshot = gpuPowerTopsSnapshot;
    }

    public BigDecimal getGpuRentPriceSnapshot() {
        return gpuRentPriceSnapshot;
    }

    public void setGpuRentPriceSnapshot(BigDecimal gpuRentPriceSnapshot) {
        this.gpuRentPriceSnapshot = gpuRentPriceSnapshot;
    }

    public Long getTokenOutputPerDaySnapshot() {
        return tokenOutputPerDaySnapshot;
    }

    public void setTokenOutputPerDaySnapshot(Long tokenOutputPerDaySnapshot) {
        this.tokenOutputPerDaySnapshot = tokenOutputPerDaySnapshot;
    }

    public String getAiModelNameSnapshot() {
        return aiModelNameSnapshot;
    }

    public void setAiModelNameSnapshot(String aiModelNameSnapshot) {
        this.aiModelNameSnapshot = aiModelNameSnapshot;
    }

    public String getAiVendorNameSnapshot() {
        return aiVendorNameSnapshot;
    }

    public void setAiVendorNameSnapshot(String aiVendorNameSnapshot) {
        this.aiVendorNameSnapshot = aiVendorNameSnapshot;
    }

    public BigDecimal getMonthlyTokenConsumptionSnapshot() {
        return monthlyTokenConsumptionSnapshot;
    }

    public void setMonthlyTokenConsumptionSnapshot(BigDecimal monthlyTokenConsumptionSnapshot) {
        this.monthlyTokenConsumptionSnapshot = monthlyTokenConsumptionSnapshot;
    }

    public BigDecimal getTokenUnitPriceSnapshot() {
        return tokenUnitPriceSnapshot;
    }

    public void setTokenUnitPriceSnapshot(BigDecimal tokenUnitPriceSnapshot) {
        this.tokenUnitPriceSnapshot = tokenUnitPriceSnapshot;
    }

    public BigDecimal getDeployFeeSnapshot() {
        return deployFeeSnapshot;
    }

    public void setDeployFeeSnapshot(BigDecimal deployFeeSnapshot) {
        this.deployFeeSnapshot = deployFeeSnapshot;
    }

    public Integer getCycleDaysSnapshot() {
        return cycleDaysSnapshot;
    }

    public void setCycleDaysSnapshot(Integer cycleDaysSnapshot) {
        this.cycleDaysSnapshot = cycleDaysSnapshot;
    }

    public BigDecimal getYieldMultiplierSnapshot() {
        return yieldMultiplierSnapshot;
    }

    public void setYieldMultiplierSnapshot(BigDecimal yieldMultiplierSnapshot) {
        this.yieldMultiplierSnapshot = yieldMultiplierSnapshot;
    }

    public BigDecimal getEarlyPenaltyRateSnapshot() {
        return earlyPenaltyRateSnapshot;
    }

    public void setEarlyPenaltyRateSnapshot(BigDecimal earlyPenaltyRateSnapshot) {
        this.earlyPenaltyRateSnapshot = earlyPenaltyRateSnapshot;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getExpectedDailyProfit() {
        return expectedDailyProfit;
    }

    public void setExpectedDailyProfit(BigDecimal expectedDailyProfit) {
        this.expectedDailyProfit = expectedDailyProfit;
    }

    public BigDecimal getExpectedTotalProfit() {
        return expectedTotalProfit;
    }

    public void setExpectedTotalProfit(BigDecimal expectedTotalProfit) {
        this.expectedTotalProfit = expectedTotalProfit;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProfitStatus() {
        return profitStatus;
    }

    public void setProfitStatus(String profitStatus) {
        this.profitStatus = profitStatus;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getMachinePayTxNo() {
        return machinePayTxNo;
    }

    public void setMachinePayTxNo(String machinePayTxNo) {
        this.machinePayTxNo = machinePayTxNo;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public LocalDateTime getApiGeneratedAt() {
        return apiGeneratedAt;
    }

    public void setApiGeneratedAt(LocalDateTime apiGeneratedAt) {
        this.apiGeneratedAt = apiGeneratedAt;
    }

    public LocalDateTime getDeployFeePaidAt() {
        return deployFeePaidAt;
    }

    public void setDeployFeePaidAt(LocalDateTime deployFeePaidAt) {
        this.deployFeePaidAt = deployFeePaidAt;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(LocalDateTime activatedAt) {
        this.activatedAt = activatedAt;
    }

    public LocalDateTime getAutoPauseAt() {
        return autoPauseAt;
    }

    public void setAutoPauseAt(LocalDateTime autoPauseAt) {
        this.autoPauseAt = autoPauseAt;
    }

    public LocalDateTime getPausedAt() {
        return pausedAt;
    }

    public void setPausedAt(LocalDateTime pausedAt) {
        this.pausedAt = pausedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getProfitStartAt() {
        return profitStartAt;
    }

    public void setProfitStartAt(LocalDateTime profitStartAt) {
        this.profitStartAt = profitStartAt;
    }

    public LocalDateTime getProfitEndAt() {
        return profitEndAt;
    }

    public void setProfitEndAt(LocalDateTime profitEndAt) {
        this.profitEndAt = profitEndAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
