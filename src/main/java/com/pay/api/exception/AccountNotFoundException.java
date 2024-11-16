package com.pay.api.exception;

public class AccountNotFoundException extends PayApiCustomException {
    public AccountNotFoundException() {
        super(ErrorCode.ACCOUNT_NOT_FOUND);
    }
}
