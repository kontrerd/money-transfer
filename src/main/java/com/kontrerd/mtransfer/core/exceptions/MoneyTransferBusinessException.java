package com.kontrerd.mtransfer.core.exceptions;

public class MoneyTransferBusinessException extends RuntimeException {
    private ErrorCode errorCode;

    public MoneyTransferBusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
