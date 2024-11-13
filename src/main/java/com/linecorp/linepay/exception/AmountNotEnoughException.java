package com.linecorp.linepay.exception;

public class AmountNotEnoughException extends LinePayCustomException {
    public AmountNotEnoughException() {
        super(ErrorCode.AMOUNT_NOT_ENOUGH);
    }
}
