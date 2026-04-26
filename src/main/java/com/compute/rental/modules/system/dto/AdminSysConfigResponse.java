package com.compute.rental.modules.system.dto;

import java.time.LocalDateTime;

public record AdminSysConfigResponse(
        Long id,
        String configKey,
        String configValue,
        String configDesc,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
