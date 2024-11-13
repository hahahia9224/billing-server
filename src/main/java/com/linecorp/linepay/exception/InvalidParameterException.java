package com.linecorp.linepay.exception;

public class InvalidParameterException extends LinePayCustomException {
    public InvalidParameterException(String message) {
        super(ErrorCode.INVALID_PARAMETER, message);
    }
}
