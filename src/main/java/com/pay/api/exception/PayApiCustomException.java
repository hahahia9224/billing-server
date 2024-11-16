package com.pay.api.exception;

import lombok.Getter;

@Getter
public class PayApiCustomException extends RuntimeException {

    private final ResponseCode responseCode;

    public PayApiCustomException(ResponseCode responseCode) {
        super(responseCode.getResultMessage());
        this.responseCode = responseCode;
    }

    public PayApiCustomException(ResponseCode responseCode, String message) {
        super(message != null ? message : responseCode.getResultMessage());
        this.responseCode = responseCode;
    }

}
