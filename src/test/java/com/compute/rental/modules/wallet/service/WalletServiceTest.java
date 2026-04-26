package com.compute.rental.modules.wallet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.compute.rental.common.enums.CommonStatus;
import com.compute.rental.common.enums.WalletBusinessType;
import com.compute.rental.modules.wallet.entity.UserWallet;
import com.compute.rental.modules.wallet.entity.WalletTransaction;
import com.compute.rental.modules.wallet.mapper.UserWalletMapper;
import com.compute.rental.modules.wallet.mapper.WalletTransactionMapper;
import java.math.BigDecimal;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private UserWalletMapper userWalletMapper;

    @Mock
    private WalletTransactionMapper walletTransactionMapper;

    @Captor
    private ArgumentCaptor<WalletTransaction> transactionCaptor;

    @InjectMocks
    private WalletService walletService;

    @BeforeAll
    static void initMybatisPlusTableInfo() {
        var configuration = new Configuration();
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(configuration, ""), UserWallet.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(configuration, ""), WalletTransaction.class);
    }

    @Test
    void creditShouldUpdateWalletAndInsertTransaction() {
        when(walletTransactionMapper.selectOne(any(Wrapper.class))).thenReturn(null);
        when(userWalletMapper.selectOne(any(Wrapper.class))).thenReturn(wallet());
        when(userWalletMapper.update(isNull(), any(Wrapper.class))).thenReturn(1);
        when(walletTransactionMapper.insert(any(WalletTransaction.class))).thenReturn(1);

        var tx = walletService.credit(1L, new BigDecimal("20.00000000"),
                WalletBusinessType.RECHARGE, "R001", "test");

        verify(walletTransactionMapper).insert(transactionCaptor.capture());
        var inserted = transactionCaptor.getValue();
        assertThat(tx).isSameAs(inserted);
        assertThat(inserted.getIdempotencyKey()).isEqualTo("RECHARGE:R001:IN");
        assertThat(inserted.getTxType()).isEqualTo("IN");
        assertThat(inserted.getAmount()).isEqualByComparingTo("20.00000000");
        assertThat(inserted.getBeforeAvailableBalance()).isEqualByComparingTo("100.00000000");
        assertThat(inserted.getAfterAvailableBalance()).isEqualByComparingTo("120.00000000");
    }

    @Test
    void duplicatedIdempotencyKeyShouldReturnExistingTransactionWithoutChangingWallet() {
        var existing = new WalletTransaction();
        existing.setIdempotencyKey("RECHARGE:R001:IN");
        when(walletTransactionMapper.selectOne(any(Wrapper.class))).thenReturn(existing);

        var result = walletService.credit(1L, new BigDecimal("20.00000000"),
                WalletBusinessType.RECHARGE, "R001", "test");

        assertThat(result).isSameAs(existing);
        verify(userWalletMapper, never()).update(any(), any());
        verify(walletTransactionMapper, never()).insert(any(WalletTransaction.class));
    }

    private UserWallet wallet() {
        var wallet = new UserWallet();
        wallet.setId(10L);
        wallet.setUserId(1L);
        wallet.setWalletNo("W001");
        wallet.setCurrency("USDT");
        wallet.setAvailableBalance(new BigDecimal("100.00000000"));
        wallet.setFrozenBalance(new BigDecimal("0.00000000"));
        wallet.setTotalRecharge(BigDecimal.ZERO);
        wallet.setTotalWithdraw(BigDecimal.ZERO);
        wallet.setTotalProfit(BigDecimal.ZERO);
        wallet.setTotalCommission(BigDecimal.ZERO);
        wallet.setStatus(CommonStatus.ENABLED.value());
        wallet.setVersionNo(0);
        return wallet;
    }
}
