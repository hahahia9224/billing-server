package com.pay.api.exception;

public class AmountNotEnoughException extends PayApiCustomException {
    public AmountNotEnoughException() {
        super(ErrorCode.AMOUNT_NOT_ENOUGH);
    }
}
