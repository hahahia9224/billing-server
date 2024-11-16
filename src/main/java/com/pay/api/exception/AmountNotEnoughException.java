package com.pay.api.exception;

public class AmountNotEnoughException extends PayApiCustomException {
    public AmountNotEnoughException() {
        super(ResponseCode.AMOUNT_NOT_ENOUGH);
    }
}
