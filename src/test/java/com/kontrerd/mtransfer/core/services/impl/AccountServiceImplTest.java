package com.kontrerd.mtransfer.core.services.impl;

import com.kontrerd.mtransfer.core.common.Callback;
import com.kontrerd.mtransfer.core.common.TransactionService;
import com.kontrerd.mtransfer.core.exceptions.AccountNotFoundExecption;
import com.kontrerd.mtransfer.core.model.Account;
import com.kontrerd.mtransfer.core.repository.AccountRepository;
import com.kontrerd.mtransfer.core.services.AccountService;
import com.kontrerd.mtransfer.core.services.HistoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.security.acl.AclNotFoundException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Mock
    private
    TransactionService transactionService;
    @Mock
    private
    AccountRepository repository;
    @Mock
    private
    HistoryService historyService;

    private AccountService accountService;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl(transactionService, historyService, repository);
    }

    @Test(expected = AccountNotFoundExecption.class)
    public void testThatExceptionThrownIfAccountNotFound() {
        accountService.getById(1L);
    }

    @Test
    public void testThatTransferProperlyChangeBalanceOnBothAccountsAndSaveResult() {
        //given
        Account acc1 = new Account(1L, "Account 1", BigDecimal.valueOf(100));
        Account acc2 = new Account(2L, "Account 2", BigDecimal.valueOf(100));
        doAnswer(invocation -> {
            invocation.getArgumentAt(0, Callback.class).execute();
            return null;
        }).when(transactionService).doInTransaction(any(Callback.class), anyListOf(Long.class));
        when(repository.getById(eq(acc1.getId()))).thenReturn(acc1);
        when(repository.getById(eq(acc2.getId()))).thenReturn(acc2);

        //when
        accountService.transfer(acc1.getId(), acc2.getId(), BigDecimal.valueOf(50));

        //then
        assertThat(acc1.getBalance(), is(BigDecimal.valueOf(50)));
        assertThat(acc2.getBalance(), is(BigDecimal.valueOf(150)));
        verify(historyService, times(2)).add(any(), any());
        verify(repository, times(2)).save(any());

    }
}