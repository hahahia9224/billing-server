package com.pay.api.exception;

import lombok.Getter;

@Getter
public class PayApiCustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public PayApiCustomException(ErrorCode errorCode) {
        super(errorCode.getResultMessage());
        this.errorCode = errorCode;
    }

    public PayApiCustomException(ErrorCode errorCode, String message) {
        super(message != null ? message : errorCode.getResultMessage());
        this.errorCode = errorCode;
    }

}
