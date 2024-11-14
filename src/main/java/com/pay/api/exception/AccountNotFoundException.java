package com.pay.api.exception;

public class AccountNotFoundException extends PayApiCustomException {
    public AccountNotFoundException() {
        super(ErrorCode.AMOUNT_NOT_ENOUGH);
    }
}
