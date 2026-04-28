package com.compute.rental.modules.order.entity;

import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("rental_profit_record")
public class RentalProfitRecord {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("profit_no")
    private String profitNo;

    @TableField("user_id")
    private Long userId;

    @TableField(exist = false)
    private String userName;

    @TableField("rental_order_id")
    private Long rentalOrderId;

    @TableField("profit_date")
    private LocalDate profitDate;

    @TableField("gpu_daily_token_snapshot")
    private Long gpuDailyTokenSnapshot;

    @TableField("token_price_snapshot")
    private BigDecimal tokenPriceSnapshot;

    @TableField("yield_multiplier_snapshot")
    private BigDecimal yieldMultiplierSnapshot;

    @TableField("base_profit_amount")
    private BigDecimal baseProfitAmount;

    @TableField("final_profit_amount")
    private BigDecimal finalProfitAmount;

    @TableField("status")
    private String status;

    @TableField("wallet_tx_no")
    private String walletTxNo;

    @TableField("commission_generated")
    private Integer commissionGenerated;

    @TableField("settled_at")
    private LocalDateTime settledAt;

    @TableField("remark")
    private String remark;

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

    public String getProfitNo() {
        return profitNo;
    }

    public void setProfitNo(String profitNo) {
        this.profitNo = profitNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getRentalOrderId() {
        return rentalOrderId;
    }

    public void setRentalOrderId(Long rentalOrderId) {
        this.rentalOrderId = rentalOrderId;
    }

    public LocalDate getProfitDate() {
        return profitDate;
    }

    public void setProfitDate(LocalDate profitDate) {
        this.profitDate = profitDate;
    }

    public Long getGpuDailyTokenSnapshot() {
        return gpuDailyTokenSnapshot;
    }

    public void setGpuDailyTokenSnapshot(Long gpuDailyTokenSnapshot) {
        this.gpuDailyTokenSnapshot = gpuDailyTokenSnapshot;
    }

    public BigDecimal getTokenPriceSnapshot() {
        return tokenPriceSnapshot;
    }

    public void setTokenPriceSnapshot(BigDecimal tokenPriceSnapshot) {
        this.tokenPriceSnapshot = tokenPriceSnapshot;
    }

    public BigDecimal getYieldMultiplierSnapshot() {
        return yieldMultiplierSnapshot;
    }

    public void setYieldMultiplierSnapshot(BigDecimal yieldMultiplierSnapshot) {
        this.yieldMultiplierSnapshot = yieldMultiplierSnapshot;
    }

    public BigDecimal getBaseProfitAmount() {
        return baseProfitAmount;
    }

    public void setBaseProfitAmount(BigDecimal baseProfitAmount) {
        this.baseProfitAmount = baseProfitAmount;
    }

    public BigDecimal getFinalProfitAmount() {
        return finalProfitAmount;
    }

    public void setFinalProfitAmount(BigDecimal finalProfitAmount) {
        this.finalProfitAmount = finalProfitAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWalletTxNo() {
        return walletTxNo;
    }

    public void setWalletTxNo(String walletTxNo) {
        this.walletTxNo = walletTxNo;
    }

    public Integer getCommissionGenerated() {
        return commissionGenerated;
    }

    public void setCommissionGenerated(Integer commissionGenerated) {
        this.commissionGenerated = commissionGenerated;
    }

    public LocalDateTime getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(LocalDateTime settledAt) {
        this.settledAt = settledAt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
