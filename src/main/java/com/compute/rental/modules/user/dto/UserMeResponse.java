package com.compute.rental.modules.user.dto;

public record UserMeResponse(
        Long id,
        String userId,
        String email,
        String userName,
        Integer status
) {
}
