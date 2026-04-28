package com.compute.rental.modules.order.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("api_deploy_order")
public class ApiDeployOrder {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("deploy_no")
    private String deployNo;

    @TableField("user_id")
    private Long userId;

    @TableField(exist = false)
    private String userName;

    @TableField("rental_order_id")
    private Long rentalOrderId;

    @TableField("api_credential_id")
    private Long apiCredentialId;

    @TableField("ai_model_id")
    private Long aiModelId;

    @TableField("model_name_snapshot")
    private String modelNameSnapshot;

    @TableField("currency")
    private String currency;

    @TableField("deploy_fee_amount")
    private BigDecimal deployFeeAmount;

    @TableField("status")
    private String status;

    @TableField("wallet_tx_no")
    private String walletTxNo;

    @TableField("paid_at")
    private LocalDateTime paidAt;

    @TableField("canceled_at")
    private LocalDateTime canceledAt;

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

    public String getDeployNo() {
        return deployNo;
    }

    public void setDeployNo(String deployNo) {
        this.deployNo = deployNo;
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

    public Long getApiCredentialId() {
        return apiCredentialId;
    }

    public void setApiCredentialId(Long apiCredentialId) {
        this.apiCredentialId = apiCredentialId;
    }

    public Long getAiModelId() {
        return aiModelId;
    }

    public void setAiModelId(Long aiModelId) {
        this.aiModelId = aiModelId;
    }

    public String getModelNameSnapshot() {
        return modelNameSnapshot;
    }

    public void setModelNameSnapshot(String modelNameSnapshot) {
        this.modelNameSnapshot = modelNameSnapshot;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getDeployFeeAmount() {
        return deployFeeAmount;
    }

    public void setDeployFeeAmount(BigDecimal deployFeeAmount) {
        this.deployFeeAmount = deployFeeAmount;
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

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
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
