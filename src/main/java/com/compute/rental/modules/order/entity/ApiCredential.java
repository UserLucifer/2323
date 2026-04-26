package com.compute.rental.modules.order.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("api_credential")
public class ApiCredential {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("credential_no")
    private String credentialNo;

    @TableField("user_id")
    private Long userId;

    @TableField("rental_order_id")
    private Long rentalOrderId;

    @TableField("api_name")
    private String apiName;

    @TableField("api_base_url")
    private String apiBaseUrl;

    @TableField("token_ciphertext")
    private String tokenCiphertext;

    @TableField("token_masked")
    private String tokenMasked;

    @TableField("model_name_snapshot")
    private String modelNameSnapshot;

    @TableField("deploy_fee_snapshot")
    private BigDecimal deployFeeSnapshot;

    @TableField("token_status")
    private String tokenStatus;

    @TableField("generated_at")
    private LocalDateTime generatedAt;

    @TableField("activation_paid_at")
    private LocalDateTime activationPaidAt;

    @TableField("activated_at")
    private LocalDateTime activatedAt;

    @TableField("auto_pause_at")
    private LocalDateTime autoPauseAt;

    @TableField("paused_at")
    private LocalDateTime pausedAt;

    @TableField("started_at")
    private LocalDateTime startedAt;

    @TableField("expired_at")
    private LocalDateTime expiredAt;

    @TableField("revoked_at")
    private LocalDateTime revokedAt;

    @TableField("mock_request_count")
    private Long mockRequestCount;

    @TableField("mock_token_display")
    private Long mockTokenDisplay;

    @TableField("mock_last_refresh_at")
    private LocalDateTime mockLastRefreshAt;

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

    public String getCredentialNo() {
        return credentialNo;
    }

    public void setCredentialNo(String credentialNo) {
        this.credentialNo = credentialNo;
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

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getTokenCiphertext() {
        return tokenCiphertext;
    }

    public void setTokenCiphertext(String tokenCiphertext) {
        this.tokenCiphertext = tokenCiphertext;
    }

    public String getTokenMasked() {
        return tokenMasked;
    }

    public void setTokenMasked(String tokenMasked) {
        this.tokenMasked = tokenMasked;
    }

    public String getModelNameSnapshot() {
        return modelNameSnapshot;
    }

    public void setModelNameSnapshot(String modelNameSnapshot) {
        this.modelNameSnapshot = modelNameSnapshot;
    }

    public BigDecimal getDeployFeeSnapshot() {
        return deployFeeSnapshot;
    }

    public void setDeployFeeSnapshot(BigDecimal deployFeeSnapshot) {
        this.deployFeeSnapshot = deployFeeSnapshot;
    }

    public String getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(String tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LocalDateTime getActivationPaidAt() {
        return activationPaidAt;
    }

    public void setActivationPaidAt(LocalDateTime activationPaidAt) {
        this.activationPaidAt = activationPaidAt;
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

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    public Long getMockRequestCount() {
        return mockRequestCount;
    }

    public void setMockRequestCount(Long mockRequestCount) {
        this.mockRequestCount = mockRequestCount;
    }

    public Long getMockTokenDisplay() {
        return mockTokenDisplay;
    }

    public void setMockTokenDisplay(Long mockTokenDisplay) {
        this.mockTokenDisplay = mockTokenDisplay;
    }

    public LocalDateTime getMockLastRefreshAt() {
        return mockLastRefreshAt;
    }

    public void setMockLastRefreshAt(LocalDateTime mockLastRefreshAt) {
        this.mockLastRefreshAt = mockLastRefreshAt;
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
