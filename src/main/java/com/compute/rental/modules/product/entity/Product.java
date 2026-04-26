package com.compute.rental.modules.product.entity;

import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("product")
public class Product {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("product_code")
    private String productCode;

    @TableField("product_name")
    private String productName;

    @TableField("machine_code")
    private String machineCode;

    @TableField("machine_alias")
    private String machineAlias;

    @TableField("region_id")
    private Long regionId;

    @TableField("gpu_model_id")
    private Long gpuModelId;

    @TableField("gpu_memory_gb")
    private Integer gpuMemoryGb;

    @TableField("gpu_power_tops")
    private BigDecimal gpuPowerTops;

    @TableField("rent_price")
    private BigDecimal rentPrice;

    @TableField("token_output_per_minute")
    private Long tokenOutputPerMinute;

    @TableField("token_output_per_day")
    private Long tokenOutputPerDay;

    @TableField("rentable_until")
    private LocalDate rentableUntil;

    @TableField("total_stock")
    private Integer totalStock;

    @TableField("available_stock")
    private Integer availableStock;

    @TableField("rented_stock")
    private Integer rentedStock;

    @TableField("cpu_model")
    private String cpuModel;

    @TableField("cpu_cores")
    private Integer cpuCores;

    @TableField("memory_gb")
    private Integer memoryGb;

    @TableField("system_disk_gb")
    private Integer systemDiskGb;

    @TableField("data_disk_gb")
    private Integer dataDiskGb;

    @TableField("max_expand_disk_gb")
    private Integer maxExpandDiskGb;

    @TableField("driver_version")
    private String driverVersion;

    @TableField("cuda_version")
    private String cudaVersion;

    @TableField("has_cache_optimization")
    private Integer hasCacheOptimization;

    @TableField("status")
    private Integer status;

    @TableField("sort_no")
    private Integer sortNo;

    @Version
    @TableField("version_no")
    private Integer versionNo;

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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineAlias() {
        return machineAlias;
    }

    public void setMachineAlias(String machineAlias) {
        this.machineAlias = machineAlias;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getGpuModelId() {
        return gpuModelId;
    }

    public void setGpuModelId(Long gpuModelId) {
        this.gpuModelId = gpuModelId;
    }

    public Integer getGpuMemoryGb() {
        return gpuMemoryGb;
    }

    public void setGpuMemoryGb(Integer gpuMemoryGb) {
        this.gpuMemoryGb = gpuMemoryGb;
    }

    public BigDecimal getGpuPowerTops() {
        return gpuPowerTops;
    }

    public void setGpuPowerTops(BigDecimal gpuPowerTops) {
        this.gpuPowerTops = gpuPowerTops;
    }

    public BigDecimal getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(BigDecimal rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Long getTokenOutputPerMinute() {
        return tokenOutputPerMinute;
    }

    public void setTokenOutputPerMinute(Long tokenOutputPerMinute) {
        this.tokenOutputPerMinute = tokenOutputPerMinute;
    }

    public Long getTokenOutputPerDay() {
        return tokenOutputPerDay;
    }

    public void setTokenOutputPerDay(Long tokenOutputPerDay) {
        this.tokenOutputPerDay = tokenOutputPerDay;
    }

    public LocalDate getRentableUntil() {
        return rentableUntil;
    }

    public void setRentableUntil(LocalDate rentableUntil) {
        this.rentableUntil = rentableUntil;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public Integer getRentedStock() {
        return rentedStock;
    }

    public void setRentedStock(Integer rentedStock) {
        this.rentedStock = rentedStock;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public Integer getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
    }

    public Integer getMemoryGb() {
        return memoryGb;
    }

    public void setMemoryGb(Integer memoryGb) {
        this.memoryGb = memoryGb;
    }

    public Integer getSystemDiskGb() {
        return systemDiskGb;
    }

    public void setSystemDiskGb(Integer systemDiskGb) {
        this.systemDiskGb = systemDiskGb;
    }

    public Integer getDataDiskGb() {
        return dataDiskGb;
    }

    public void setDataDiskGb(Integer dataDiskGb) {
        this.dataDiskGb = dataDiskGb;
    }

    public Integer getMaxExpandDiskGb() {
        return maxExpandDiskGb;
    }

    public void setMaxExpandDiskGb(Integer maxExpandDiskGb) {
        this.maxExpandDiskGb = maxExpandDiskGb;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public String getCudaVersion() {
        return cudaVersion;
    }

    public void setCudaVersion(String cudaVersion) {
        this.cudaVersion = cudaVersion;
    }

    public Integer getHasCacheOptimization() {
        return hasCacheOptimization;
    }

    public void setHasCacheOptimization(Integer hasCacheOptimization) {
        this.hasCacheOptimization = hasCacheOptimization;
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

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
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
