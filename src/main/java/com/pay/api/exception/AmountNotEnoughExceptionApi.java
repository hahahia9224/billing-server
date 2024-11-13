package com.pay.api.exception;

public class AmountNotEnoughExceptionApi extends PayApiCustomException {
    public AmountNotEnoughExceptionApi() {
        super(ErrorCode.AMOUNT_NOT_ENOUGH);
    }
}
