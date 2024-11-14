package com.pay.api.exception;

public class InvalidParameterException extends PayApiCustomException {
    public InvalidParameterException(String message) {
        super(ErrorCode.INVALID_PARAMETER, message);
    }
}
