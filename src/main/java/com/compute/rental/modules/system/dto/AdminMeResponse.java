package com.compute.rental.modules.system.dto;

public record AdminMeResponse(
        Long adminId,
        String username,
        String nickname,
        Integer status,
        String role
) {
}
