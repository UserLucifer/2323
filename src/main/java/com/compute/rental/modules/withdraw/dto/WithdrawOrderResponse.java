package com.compute.rental.modules.withdraw.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WithdrawOrderResponse(
        String withdrawNo,
        String currency,
        String withdrawMethod,
        String network,
        String accountName,
        String accountNo,
        BigDecimal applyAmount,
        BigDecimal feeAmount,
        BigDecimal actualAmount,
        String status,
        String freezeTxNo,
        String unfreezeTxNo,
        String paidTxNo,
        Long reviewedBy,
        LocalDateTime reviewedAt,
        String reviewRemark,
        LocalDateTime paidAt,
        String payProofNo,
        LocalDateTime createdAt
) {
}
