package com.kontrerd.mtransfer.core.services;

import com.kontrerd.mtransfer.core.model.Account;
import org.jvnet.hk2.annotations.Contract;

import java.math.BigDecimal;
import java.util.List;

@Contract
public interface AccountService {
    Account getById(Long id);

    List<Account> getAll(String sortBy, Long page, Long pageSize);

    void create(Account account);

    void create(List<Account> account);

    Account charge(Long id, BigDecimal amount);

    Account withdraw(Long id, BigDecimal amount);

    void transfer(Long srcId, Long destId, BigDecimal amount);
}
