package com.linecorp.linepay.config;

import com.linecorp.linepay.exception.ErrorCode;
import com.linecorp.linepay.exception.LinePayCustomException;
import com.linecorp.linepay.model.LineApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LinePayExceptionHandler {

    @ExceptionHandler(LinePayCustomException.class)
    public ResponseEntity<LineApiResponse> handleLinePayException(Exception e) {

        ErrorCode errorCode = ErrorCode.UNKNOWN_EXCEPTION;

        if (e instanceof LinePayCustomException linePayCustomException) {
            errorCode = linePayCustomException.getErrorCode();
        }

        // 별도로 주입 받은 getMessage 가 없다면, ErrorCode 내 resultMessage 사용
        String errorMessage = (e.getMessage() != null) ? e.getMessage() : errorCode.getResultMessage();

        LineApiResponse errorResponse = LineApiResponse.errorFrom(errorCode, errorMessage);
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }
}
