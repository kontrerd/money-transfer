package com.kontrerd.mtransfer.core.common;

import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface TransactionService {
    void doInTransaction(Callback callback, List<Long> identifiers);
}
