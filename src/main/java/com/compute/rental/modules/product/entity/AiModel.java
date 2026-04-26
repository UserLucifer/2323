package com.compute.rental.modules.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("ai_model")
public class AiModel {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("model_code")
    private String modelCode;

    @TableField("model_name")
    private String modelName;

    @TableField("vendor_name")
    private String vendorName;

    @TableField("logo_url")
    private String logoUrl;

    @TableField("monthly_token_consumption_trillion")
    private BigDecimal monthlyTokenConsumptionTrillion;

    @TableField("token_unit_price")
    private BigDecimal tokenUnitPrice;

    @TableField("deploy_tech_fee")
    private BigDecimal deployTechFee;

    @TableField("status")
    private Integer status;

    @TableField("sort_no")
    private Integer sortNo;

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

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public BigDecimal getMonthlyTokenConsumptionTrillion() {
        return monthlyTokenConsumptionTrillion;
    }

    public void setMonthlyTokenConsumptionTrillion(BigDecimal monthlyTokenConsumptionTrillion) {
        this.monthlyTokenConsumptionTrillion = monthlyTokenConsumptionTrillion;
    }

    public BigDecimal getTokenUnitPrice() {
        return tokenUnitPrice;
    }

    public void setTokenUnitPrice(BigDecimal tokenUnitPrice) {
        this.tokenUnitPrice = tokenUnitPrice;
    }

    public BigDecimal getDeployTechFee() {
        return deployTechFee;
    }

    public void setDeployTechFee(BigDecimal deployTechFee) {
        this.deployTechFee = deployTechFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
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
