package com.kontrerd.mtransfer.core.repository.impl;

import com.kontrerd.mtransfer.core.model.AccountHistoricItem;
import com.kontrerd.mtransfer.core.model.Operation;
import com.kontrerd.mtransfer.core.repository.AccountHistoryRepository;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class AccountHistoryRepositoryImplTest {

    @Test
    public void testGetAccountHistoryById() {
        //given
        AccountHistoryRepository repository = new AccountHistoryRepositoryImpl();
        repository.add(AccountHistoricItem.builder()
                .accountId(1L)
                .balanceAfter(BigDecimal.valueOf(100))
                .operation(Operation.CHARGE)
                .build());
        repository.add(AccountHistoricItem.builder()
                .accountId(1L)
                .balanceAfter(BigDecimal.valueOf(50))
                .operation(Operation.WITHDRAW)
                .build());
        repository.add(AccountHistoricItem.builder()
                .accountId(2L)
                .balanceAfter(BigDecimal.valueOf(100))
                .operation(Operation.CHARGE)
                .build());

        //when
        List<AccountHistoricItem> history = repository.get(1L);
        assertThat(history, hasSize(2));
    }
}