package com.compute.rental.modules.wallet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("withdraw_order")
public class WithdrawOrder {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("withdraw_no")
    private String withdrawNo;

    @TableField("user_id")
    private Long userId;

    @TableField("wallet_id")
    private Long walletId;

    @TableField("currency")
    private String currency;

    @TableField("withdraw_method")
    private String withdrawMethod;

    @TableField("network")
    private String network;

    @TableField("account_name")
    private String accountName;

    @TableField("account_no")
    private String accountNo;

    @TableField("apply_amount")
    private BigDecimal applyAmount;

    @TableField("fee_amount")
    private BigDecimal feeAmount;

    @TableField("actual_amount")
    private BigDecimal actualAmount;

    @TableField("status")
    private String status;

    @TableField("freeze_tx_no")
    private String freezeTxNo;

    @TableField("unfreeze_tx_no")
    private String unfreezeTxNo;

    @TableField("paid_tx_no")
    private String paidTxNo;

    @TableField("reviewed_by")
    private Long reviewedBy;

    @TableField("reviewed_at")
    private LocalDateTime reviewedAt;

    @TableField("review_remark")
    private String reviewRemark;

    @TableField("paid_at")
    private LocalDateTime paidAt;

    @TableField("pay_proof_no")
    private String payProofNo;

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

    public String getWithdrawNo() {
        return withdrawNo;
    }

    public void setWithdrawNo(String withdrawNo) {
        this.withdrawNo = withdrawNo;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getWithdrawMethod() {
        return withdrawMethod;
    }

    public void setWithdrawMethod(String withdrawMethod) {
        this.withdrawMethod = withdrawMethod;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFreezeTxNo() {
        return freezeTxNo;
    }

    public void setFreezeTxNo(String freezeTxNo) {
        this.freezeTxNo = freezeTxNo;
    }

    public String getUnfreezeTxNo() {
        return unfreezeTxNo;
    }

    public void setUnfreezeTxNo(String unfreezeTxNo) {
        this.unfreezeTxNo = unfreezeTxNo;
    }

    public String getPaidTxNo() {
        return paidTxNo;
    }

    public void setPaidTxNo(String paidTxNo) {
        this.paidTxNo = paidTxNo;
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

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public String getPayProofNo() {
        return payProofNo;
    }

    public void setPayProofNo(String payProofNo) {
        this.payProofNo = payProofNo;
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
