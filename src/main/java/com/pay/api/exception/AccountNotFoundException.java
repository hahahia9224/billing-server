package com.pay.api.exception;

public class AccountNotFoundException extends PayApiCustomException {
    public AccountNotFoundException() {
        super(ResponseCode.ACCOUNT_NOT_FOUND);
    }
}
