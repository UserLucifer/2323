package com.compute.rental.modules.wallet.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("wallet_transaction")
public class WalletTransaction {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("tx_no")
    private String txNo;

    @TableField("idempotency_key")
    private String idempotencyKey;

    @TableField("user_id")
    private Long userId;

    @TableField(exist = false)
    private String userName;

    @TableField("wallet_id")
    private Long walletId;

    @TableField("currency")
    private String currency;

    @TableField("tx_type")
    private String txType;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("before_available_balance")
    private BigDecimal beforeAvailableBalance;

    @TableField("after_available_balance")
    private BigDecimal afterAvailableBalance;

    @TableField("before_frozen_balance")
    private BigDecimal beforeFrozenBalance;

    @TableField("after_frozen_balance")
    private BigDecimal afterFrozenBalance;

    @TableField("biz_type")
    private String bizType;

    @TableField("biz_order_no")
    private String bizOrderNo;

    @TableField("remark")
    private String remark;

    @TableField("created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxNo() {
        return txNo;
    }

    public void setTxNo(String txNo) {
        this.txNo = txNo;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
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

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBeforeAvailableBalance() {
        return beforeAvailableBalance;
    }

    public void setBeforeAvailableBalance(BigDecimal beforeAvailableBalance) {
        this.beforeAvailableBalance = beforeAvailableBalance;
    }

    public BigDecimal getAfterAvailableBalance() {
        return afterAvailableBalance;
    }

    public void setAfterAvailableBalance(BigDecimal afterAvailableBalance) {
        this.afterAvailableBalance = afterAvailableBalance;
    }

    public BigDecimal getBeforeFrozenBalance() {
        return beforeFrozenBalance;
    }

    public void setBeforeFrozenBalance(BigDecimal beforeFrozenBalance) {
        this.beforeFrozenBalance = beforeFrozenBalance;
    }

    public BigDecimal getAfterFrozenBalance() {
        return afterFrozenBalance;
    }

    public void setAfterFrozenBalance(BigDecimal afterFrozenBalance) {
        this.afterFrozenBalance = afterFrozenBalance;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
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

}
