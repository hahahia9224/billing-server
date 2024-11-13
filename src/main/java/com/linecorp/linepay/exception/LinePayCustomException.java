package com.linecorp.linepay.exception;

import lombok.Getter;

@Getter
public class LinePayCustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public LinePayCustomException(ErrorCode errorCode) {
        super(errorCode.getResultMessage());
        this.errorCode = errorCode;
    }

    public LinePayCustomException(ErrorCode errorCode, String message) {
        super(message != null ? message : errorCode.getResultMessage());
        this.errorCode = errorCode;
    }

}
