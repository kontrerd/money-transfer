package com.kontrerd.mtransfer.core.repository;

import com.kontrerd.mtransfer.core.model.AccountHistoricItem;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface AccountHistoryRepository {
    void add(AccountHistoricItem historicItem);
    List<AccountHistoricItem> get(Long accountId);
}
