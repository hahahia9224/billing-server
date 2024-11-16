package com.pay.api.exception;

public class InvalidParameterException extends PayApiCustomException {
    public InvalidParameterException(String message) {
        super(ResponseCode.INVALID_PARAMETER, message);
    }
}
