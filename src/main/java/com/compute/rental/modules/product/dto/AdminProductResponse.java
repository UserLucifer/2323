package com.compute.rental.modules.product.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminProductResponse(
        Long id,
        String productCode,
        String productName,
        String machineCode,
        String machineAlias,
        Long regionId,
        String regionName,
        Long gpuModelId,
        String gpuModelName,
        Integer gpuMemoryGb,
        BigDecimal gpuPowerTops,
        BigDecimal rentPrice,
        Long tokenOutputPerMinute,
        Long tokenOutputPerDay,
        LocalDate rentableUntil,
        Integer totalStock,
        Integer availableStock,
        Integer rentedStock,
        String cpuModel,
        Integer cpuCores,
        Integer memoryGb,
        Integer systemDiskGb,
        Integer dataDiskGb,
        Integer maxExpandDiskGb,
        String driverVersion,
        String cudaVersion,
        Integer hasCacheOptimization,
        Integer status,
        Integer sortNo,
        Integer versionNo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
