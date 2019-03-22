package com.kontrerd.mtransfer.core.exceptions;

public enum ErrorCode {
    ACCOUNT_NOT_FOUND(1), INSUFFICIENT_FUNDS(2), REQUEST_VALIDATION_ERROR(3);

    private final int value;

    ErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
