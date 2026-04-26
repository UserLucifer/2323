package com.compute.rental.modules.commission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("commission_record")
public class CommissionRecord {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("commission_no")
    private String commissionNo;

    @TableField("benefit_user_id")
    private Long benefitUserId;

    @TableField("source_user_id")
    private Long sourceUserId;

    @TableField("source_order_id")
    private Long sourceOrderId;

    @TableField("source_profit_id")
    private Long sourceProfitId;

    @TableField("level_no")
    private Integer levelNo;

    @TableField("currency")
    private String currency;

    @TableField("source_profit_amount")
    private BigDecimal sourceProfitAmount;

    @TableField("commission_rate_snapshot")
    private BigDecimal commissionRateSnapshot;

    @TableField("commission_amount")
    private BigDecimal commissionAmount;

    @TableField("status")
    private String status;

    @TableField("wallet_tx_no")
    private String walletTxNo;

    @TableField("settled_at")
    private LocalDateTime settledAt;

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

    public String getCommissionNo() {
        return commissionNo;
    }

    public void setCommissionNo(String commissionNo) {
        this.commissionNo = commissionNo;
    }

    public Long getBenefitUserId() {
        return benefitUserId;
    }

    public void setBenefitUserId(Long benefitUserId) {
        this.benefitUserId = benefitUserId;
    }

    public Long getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(Long sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public Long getSourceOrderId() {
        return sourceOrderId;
    }

    public void setSourceOrderId(Long sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
    }

    public Long getSourceProfitId() {
        return sourceProfitId;
    }

    public void setSourceProfitId(Long sourceProfitId) {
        this.sourceProfitId = sourceProfitId;
    }

    public Integer getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(Integer levelNo) {
        this.levelNo = levelNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSourceProfitAmount() {
        return sourceProfitAmount;
    }

    public void setSourceProfitAmount(BigDecimal sourceProfitAmount) {
        this.sourceProfitAmount = sourceProfitAmount;
    }

    public BigDecimal getCommissionRateSnapshot() {
        return commissionRateSnapshot;
    }

    public void setCommissionRateSnapshot(BigDecimal commissionRateSnapshot) {
        this.commissionRateSnapshot = commissionRateSnapshot;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
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

    public LocalDateTime getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(LocalDateTime settledAt) {
        this.settledAt = settledAt;
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
