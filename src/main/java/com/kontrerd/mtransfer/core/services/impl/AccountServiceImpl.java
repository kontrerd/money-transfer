package com.kontrerd.mtransfer.core.services.impl;

import com.kontrerd.mtransfer.core.common.TransactionService;
import com.kontrerd.mtransfer.core.exceptions.AccountNotFoundExecption;
import com.kontrerd.mtransfer.core.exceptions.InsufficientFundsException;
import com.kontrerd.mtransfer.core.model.Account;
import com.kontrerd.mtransfer.core.model.Operation;
import com.kontrerd.mtransfer.core.repository.AccountRepository;
import com.kontrerd.mtransfer.core.services.AccountService;
import com.kontrerd.mtransfer.core.services.HistoryService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

@Service
public class AccountServiceImpl implements AccountService {

    private static final long DEFAULT_PAGE_SIZE = 10L;
    private static final String DEFAULT_SORT_COLUMN = "id";

    private TransactionService transactionService;
    private HistoryService historyService;
    private AccountRepository repository;

    @Inject
    public AccountServiceImpl(TransactionService transactionService,
                              HistoryService historyService,
                              AccountRepository repository) {
        this.transactionService = transactionService;
        this.historyService = historyService;
        this.repository = repository;
    }

    @Override
    public Account getById(Long id) {
        return ofNullable(repository.getById(id))
                .orElseThrow(() -> new AccountNotFoundExecption(id));
    }

    @Override
    public List<Account> getAll(String sortBy, Long page, Long pageSize) {
        return repository.getAll(ofNullable(sortBy).orElse(DEFAULT_SORT_COLUMN),
                ofNullable(page).orElse(1L),
                ofNullable(pageSize).orElse(DEFAULT_PAGE_SIZE));
    }

    @Override
    public Account create(Account account) {
        return repository.save(account);
    }

    @Override
    public void create(List<Account> account) {
        account.forEach(repository::save);
    }

    @Override
    public Account charge(Long id, BigDecimal amount) {
        transactionService.doInTransaction(() -> charge(getById(id), amount), singletonList(id));
        return getById(id);
    }

    @Override
    public Account withdraw(Long id, BigDecimal amount) {
        transactionService.doInTransaction(() -> withdraw(getById(id), amount), singletonList(id));
        return getById(id);
    }

    @Override
    public void transfer(Long srcId, Long destId, BigDecimal amount) {
        transactionService.doInTransaction(() -> doTransfer(srcId, destId, amount), asList(srcId, destId));
    }

    private void doTransfer(Long srcId, Long destId, BigDecimal amount) {
        Account srcAccount = getById(srcId);
        Account destAccount = getById(destId);
        withdraw(srcAccount, amount);
        charge(destAccount, amount);
    }

    private void withdraw(Account account, BigDecimal amount) {
        BigDecimal newBalance = ofNullable(account.getBalance())
                .filter(balance -> balance.compareTo(amount) >= 0)
                .orElseThrow(() -> new InsufficientFundsException(account.getId()))
                .subtract(amount);
        account.setBalance(newBalance);
        repository.save(account);
        historyService.add(account, Operation.WITHDRAW);
    }

    private void charge(Account account, BigDecimal amount) {
        BigDecimal newBalance = ofNullable(account.getBalance())
                .orElse(BigDecimal.ZERO)
                .add(amount);
        account.setBalance(newBalance);
        repository.save(account);
        historyService.add(account, Operation.CHARGE);
    }

}
