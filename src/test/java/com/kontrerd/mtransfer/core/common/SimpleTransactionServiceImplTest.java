package com.kontrerd.mtransfer.core.common;

import com.kontrerd.mtransfer.core.model.Account;
import org.glassfish.grizzly.threadpool.FixedThreadPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SimpleTransactionServiceImplTest {
    private TransactionService transactionService;
    private ExecutorService executorService;

    @Before
    public void setUp() throws Exception {
        transactionService = new SimpleTransactionServiceImpl();
        executorService = Executors.newFixedThreadPool(2);
    }

    @After
    public void tearDown() throws Exception {
        executorService.shutdown();
    }

    @Test
    public void testThatAccountLocked() {
        //given
        Account acc1 = new Account(1L, "Account 1", BigDecimal.valueOf(100) );
        double newBalance1 = 101D;
        double newBalance2 = 102D;

        //when
        runAccountsBalanceChangingWithDelays(0, 500, newBalance1, acc1);
        runAccountsBalanceChangingWithDelays(100, 0, newBalance2, acc1);
        delay(200);

        //then
        assertThat(acc1.getBalance().doubleValue(), is(newBalance1));
        delay(1000);
        assertThat(acc1.getBalance().doubleValue(), is(newBalance2));
    }

    @Test
    public void testThatMultipleAccountsLocked() {
        //given
        Account acc1 = new Account(1L, "Account 1", BigDecimal.valueOf(100) );
        Account acc2 = new Account(2L, "Account 2", BigDecimal.valueOf(100) );
        double newBalance1 = 101D;
        double newBalance2 = 102D;

        //when
        runAccountsBalanceChangingWithDelays(0, 500, newBalance1, acc1, acc2);
        runAccountsBalanceChangingWithDelays(100, 0, newBalance2, acc1, acc2);
        delay(200);

        //then
        assertThat(acc1.getBalance().doubleValue(), is(newBalance1));
    }

    private void runAccountsBalanceChangingWithDelays(int delayBefore, int operationDelay, double newBalance, Account... accounts) {
        executorService.submit(() -> {
            delay(delayBefore);
            transactionService.doInTransaction(() -> {
                Arrays.stream(accounts).forEach(acc -> acc.setBalance(BigDecimal.valueOf(newBalance)));
                delay(operationDelay);
            }, Arrays.stream(accounts).map(Account::getId).collect(Collectors.toList()));
        });

    }

    private void delay(int timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {}
    }
}