package com.kontrerd.mtransfer.core.repository;

import com.kontrerd.mtransfer.core.model.Account;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface AccountRepository {

    Account getById(Long id);

    List<Account> getAll(String sortBy, long page, long pageSize);

    Account save(Account account);
}
