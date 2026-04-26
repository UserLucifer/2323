package com.compute.rental.modules.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("rental_cycle_rule")
public class RentalCycleRule {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("cycle_code")
    private String cycleCode;

    @TableField("cycle_name")
    private String cycleName;

    @TableField("cycle_days")
    private Integer cycleDays;

    @TableField("yield_multiplier")
    private BigDecimal yieldMultiplier;

    @TableField("early_penalty_rate")
    private BigDecimal earlyPenaltyRate;

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

    public String getCycleCode() {
        return cycleCode;
    }

    public void setCycleCode(String cycleCode) {
        this.cycleCode = cycleCode;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public Integer getCycleDays() {
        return cycleDays;
    }

    public void setCycleDays(Integer cycleDays) {
        this.cycleDays = cycleDays;
    }

    public BigDecimal getYieldMultiplier() {
        return yieldMultiplier;
    }

    public void setYieldMultiplier(BigDecimal yieldMultiplier) {
        this.yieldMultiplier = yieldMultiplier;
    }

    public BigDecimal getEarlyPenaltyRate() {
        return earlyPenaltyRate;
    }

    public void setEarlyPenaltyRate(BigDecimal earlyPenaltyRate) {
        this.earlyPenaltyRate = earlyPenaltyRate;
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
