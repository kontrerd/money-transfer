package com.kontrerd.mtransfer.core.repository.impl;

import com.kontrerd.mtransfer.core.model.Account;
import com.kontrerd.mtransfer.core.repository.AccountRepository;
import org.jvnet.hk2.annotations.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
public class InMemoryAccountRepository implements AccountRepository {
    private ConcurrentHashMap<Long, Account> data = new ConcurrentHashMap<>();

    @Override
    public Account getById(Long id) {
        return copy(data.get(id));
    }

    @Override
    public List<Account> getAll(String sortBy, long page, long pageSize) {
        return data.values().stream()
                .sorted(getComparator(sortBy))
                .skip((page - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Account account) {
        data.put(account.getId(), account);
    }

    private Comparator<? super Account> getComparator(String sortBy) {
        switch (sortBy) {
            case "name": return Comparator.comparing(Account::getName);
            case "balance": return Comparator.comparing(Account::getBalance);
            default: return Comparator.comparingLong(Account::getId);
        }
    }

    private Account copy(Account account) {
        return ofNullable(account).map(acc -> new Account(acc.getId(), acc.getName(), acc.getBalance())).orElse(null);
    }
}
