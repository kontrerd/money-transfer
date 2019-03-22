package com.kontrerd.mtransfer.core.exceptions;

import java.util.function.Supplier;

public class ValidationException extends MoneyTransferBusinessException {
    public ValidationException() {
        super(ErrorCode.REQUEST_VALIDATION_ERROR, "Empty request or mandatory request attributes absent");
    }
}
