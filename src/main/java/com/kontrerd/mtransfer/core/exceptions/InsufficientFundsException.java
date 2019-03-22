package com.kontrerd.mtransfer.core.exceptions;

public class InsufficientFundsException extends MoneyTransferBusinessException {
    public InsufficientFundsException(Long id) {
        super(ErrorCode.INSUFFICIENT_FUNDS, "Insufficient funds on the account! Account id " + id);
    }
}
