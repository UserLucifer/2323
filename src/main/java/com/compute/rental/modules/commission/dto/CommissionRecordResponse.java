package com.compute.rental.modules.commission.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CommissionRecordResponse(
        String commissionNo,
        Long sourceUserId,
        Long sourceOrderId,
        Long sourceProfitId,
        Integer levelNo,
        BigDecimal sourceProfitAmount,
        BigDecimal commissionRateSnapshot,
        BigDecimal commissionAmount,
        String status,
        String walletTxNo,
        LocalDateTime settledAt,
        LocalDateTime createdAt
) {
}
