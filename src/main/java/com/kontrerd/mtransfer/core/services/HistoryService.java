package com.kontrerd.mtransfer.core.services;

import com.kontrerd.mtransfer.core.model.Account;
import com.kontrerd.mtransfer.core.model.AccountHistoricItem;
import com.kontrerd.mtransfer.core.model.Operation;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface HistoryService {
    void add(Account account, Operation operation);

    List<AccountHistoricItem> get(Long accountId);
}
