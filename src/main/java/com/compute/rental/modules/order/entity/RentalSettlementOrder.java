package com.compute.rental.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("rental_settlement_order")
public class RentalSettlementOrder {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("settlement_no")
    private String settlementNo;

    @TableField("user_id")
    private Long userId;

    @TableField("rental_order_id")
    private Long rentalOrderId;

    @TableField("settlement_type")
    private String settlementType;

    @TableField("currency")
    private String currency;

    @TableField("principal_amount")
    private BigDecimal principalAmount;

    @TableField("profit_amount")
    private BigDecimal profitAmount;

    @TableField("penalty_amount")
    private BigDecimal penaltyAmount;

    @TableField("actual_settle_amount")
    private BigDecimal actualSettleAmount;

    @TableField("status")
    private String status;

    @TableField("reviewed_by")
    private Long reviewedBy;

    @TableField("reviewed_at")
    private LocalDateTime reviewedAt;

    @TableField("settled_at")
    private LocalDateTime settledAt;

    @TableField("wallet_tx_no")
    private String walletTxNo;

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

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRentalOrderId() {
        return rentalOrderId;
    }

    public void setRentalOrderId(Long rentalOrderId) {
        this.rentalOrderId = rentalOrderId;
    }

    public String getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getActualSettleAmount() {
        return actualSettleAmount;
    }

    public void setActualSettleAmount(BigDecimal actualSettleAmount) {
        this.actualSettleAmount = actualSettleAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Long reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public LocalDateTime getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(LocalDateTime settledAt) {
        this.settledAt = settledAt;
    }

    public String getWalletTxNo() {
        return walletTxNo;
    }

    public void setWalletTxNo(String walletTxNo) {
        this.walletTxNo = walletTxNo;
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
