package com.compute.rental.modules.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApiDeployOrderResponse(
        String deployNo,
        String orderNo,
        String credentialNo,
        String modelNameSnapshot,
        BigDecimal deployFeeAmount,
        String status,
        String walletTxNo,
        LocalDateTime paidAt,
        LocalDateTime createdAt
) {
}
