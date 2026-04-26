package com.compute.rental.modules.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RentalOrderSummaryResponse(
        String orderNo,
        String productNameSnapshot,
        String machineCodeSnapshot,
        String machineAliasSnapshot,
        String aiModelNameSnapshot,
        Integer cycleDaysSnapshot,
        BigDecimal orderAmount,
        BigDecimal expectedDailyProfit,
        BigDecimal expectedTotalProfit,
        String orderStatus,
        String profitStatus,
        String settlementStatus,
        LocalDateTime createdAt,
        LocalDateTime paidAt,
        LocalDateTime apiGeneratedAt,
        LocalDateTime deployFeePaidAt,
        LocalDateTime activatedAt,
        LocalDateTime autoPauseAt,
        LocalDateTime pausedAt,
        LocalDateTime startedAt,
        LocalDateTime profitStartAt,
        LocalDateTime profitEndAt
) {
}
