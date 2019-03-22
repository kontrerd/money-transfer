package com.kontrerd.mtransfer.core.repository.impl;

import com.kontrerd.mtransfer.core.model.AccountHistoricItem;
import com.kontrerd.mtransfer.core.repository.AccountHistoryRepository;
import org.jvnet.hk2.annotations.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Service
public class AccountHistoryRepositoryImpl implements AccountHistoryRepository {
    private ConcurrentHashMap<Long, List<AccountHistoricItem>> data = new ConcurrentHashMap<>();

    @Override
    public void add(AccountHistoricItem historicItem) {
        data.compute(historicItem.getAccountId(),
                (id, history) -> {
                    history = ofNullable(history).orElse(new LinkedList<>());
                    history.add(historicItem);
                    return history;
                });
    }

    @Override
    public List<AccountHistoricItem> get(Long accountId) {
        return data.get(accountId);
    }
}
