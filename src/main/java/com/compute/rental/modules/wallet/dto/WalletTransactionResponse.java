package com.compute.rental.modules.wallet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletTransactionResponse(
        String txNo,
        String txType,
        BigDecimal amount,
        BigDecimal beforeAvailableBalance,
        BigDecimal afterAvailableBalance,
        BigDecimal beforeFrozenBalance,
        BigDecimal afterFrozenBalance,
        String bizType,
        String bizOrderNo,
        String remark,
        LocalDateTime createdAt
) {
}
