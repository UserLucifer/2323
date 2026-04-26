package com.compute.rental.modules.wallet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("recharge_order")
public class RechargeOrder {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("recharge_no")
    private String rechargeNo;

    @TableField("user_id")
    private Long userId;

    @TableField("wallet_id")
    private Long walletId;

    @TableField("channel_id")
    private Long channelId;

    @TableField("currency")
    private String currency;

    @TableField("channel_name_snapshot")
    private String channelNameSnapshot;

    @TableField("network_snapshot")
    private String networkSnapshot;

    @TableField("display_url_snapshot")
    private String displayUrlSnapshot;

    @TableField("account_no_snapshot")
    private String accountNoSnapshot;

    @TableField("apply_amount")
    private BigDecimal applyAmount;

    @TableField("actual_amount")
    private BigDecimal actualAmount;

    @TableField("external_tx_no")
    private String externalTxNo;

    @TableField("payment_proof_url")
    private String paymentProofUrl;

    @TableField("user_remark")
    private String userRemark;

    @TableField("status")
    private String status;

    @TableField("reviewed_by")
    private Long reviewedBy;

    @TableField("reviewed_at")
    private LocalDateTime reviewedAt;

    @TableField("review_remark")
    private String reviewRemark;

    @TableField("credited_at")
    private LocalDateTime creditedAt;

    @TableField("wallet_tx_no")
    private String walletTxNo;

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

    public String getRechargeNo() {
        return rechargeNo;
    }

    public void setRechargeNo(String rechargeNo) {
        this.rechargeNo = rechargeNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getChannelNameSnapshot() {
        return channelNameSnapshot;
    }

    public void setChannelNameSnapshot(String channelNameSnapshot) {
        this.channelNameSnapshot = channelNameSnapshot;
    }

    public String getNetworkSnapshot() {
        return networkSnapshot;
    }

    public void setNetworkSnapshot(String networkSnapshot) {
        this.networkSnapshot = networkSnapshot;
    }

    public String getDisplayUrlSnapshot() {
        return displayUrlSnapshot;
    }

    public void setDisplayUrlSnapshot(String displayUrlSnapshot) {
        this.displayUrlSnapshot = displayUrlSnapshot;
    }

    public String getAccountNoSnapshot() {
        return accountNoSnapshot;
    }

    public void setAccountNoSnapshot(String accountNoSnapshot) {
        this.accountNoSnapshot = accountNoSnapshot;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getExternalTxNo() {
        return externalTxNo;
    }

    public void setExternalTxNo(String externalTxNo) {
        this.externalTxNo = externalTxNo;
    }

    public String getPaymentProofUrl() {
        return paymentProofUrl;
    }

    public void setPaymentProofUrl(String paymentProofUrl) {
        this.paymentProofUrl = paymentProofUrl;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
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

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
    }

    public LocalDateTime getCreditedAt() {
        return creditedAt;
    }

    public void setCreditedAt(LocalDateTime creditedAt) {
        this.creditedAt = creditedAt;
    }

    public String getWalletTxNo() {
        return walletTxNo;
    }

    public void setWalletTxNo(String walletTxNo) {
        this.walletTxNo = walletTxNo;
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
