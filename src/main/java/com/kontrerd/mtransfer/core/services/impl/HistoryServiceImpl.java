package com.kontrerd.mtransfer.core.services.impl;

import com.kontrerd.mtransfer.core.exceptions.AccountNotFoundExecption;
import com.kontrerd.mtransfer.core.model.Account;
import com.kontrerd.mtransfer.core.model.AccountHistoricItem;
import com.kontrerd.mtransfer.core.model.Operation;
import com.kontrerd.mtransfer.core.repository.AccountHistoryRepository;
import com.kontrerd.mtransfer.core.services.HistoryService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Inject
    private AccountHistoryRepository repository;

    @Override
    public void add(Account account, Operation operation) {
        repository.add(AccountHistoricItem.builder()
                .accountId(account.getId())
                .operation(operation)
                .balanceAfter(account.getBalance())
                .date(new Date())
                .build());
    }

    @Override
    public List<AccountHistoricItem> get(Long accountId) {
        return ofNullable(repository.get(accountId))
                .orElseThrow(() -> new AccountNotFoundExecption(accountId));
    }
}
