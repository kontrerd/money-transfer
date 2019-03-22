package com.kontrerd.mtransfer.core.exceptions;

public class AccountNotFoundExecption extends MoneyTransferBusinessException {
    public AccountNotFoundExecption(Long id) {
        super(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found! Account id " + id);
    }
}
