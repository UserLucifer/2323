package com.compute.rental.modules.commission.dto;

import java.time.LocalDateTime;

public record TeamMemberResponse(
        String userId,
        String email,
        String nickname,
        Integer status,
        Integer levelDepth,
        LocalDateTime createdAt
) {
}
