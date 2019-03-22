package com.kontrerd.mtransfer.core.common;

import org.jvnet.hk2.annotations.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
public class SimpleTransactionServiceImpl implements TransactionService {

    private ConcurrentHashMap<Object, Lock> locks = new ConcurrentHashMap<>();

    @Override
    public void doInTransaction(Callback callback, List<Long> identifiers) {
        List<Lock> acquiredLocks = null;
        try {
            acquiredLocks = lockAccounts(identifiers);
            callback.execute();
        } finally {
            unlockAccounts(acquiredLocks);
        }
    }

    private List<Lock> lockAccounts(List<Long> identifiers) {
        return ofNullable(identifiers).orElseThrow(IllegalArgumentException::new)
                .stream()
                .sorted()
                .map(this::getLock)
                .collect(Collectors.toList());
    }

    private void unlockAccounts(List<Lock> acquiredLocks) {
        ofNullable(acquiredLocks).ifPresent(locks -> locks.forEach(Lock::unlock));
    }

    private Lock getLock(Long id) {
        Lock lock = locks.computeIfAbsent(id, o -> new ReentrantLock());
        lock.lock();
        return lock;
    }
}
