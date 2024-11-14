package com.pay.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 파라미터 검증 실패
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "4000", "invalid parameter"),

    // Promotion Type 검증 실패
    INVALID_PROMOTION_TYPE(HttpStatus.BAD_REQUEST, "4000", "invalid promotion type"),

    // 잔액 부족
    AMOUNT_NOT_ENOUGH(HttpStatus.FORBIDDEN, "4444", "amount not enough"),

    // 계좌 정보 없음
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "9999", "account not found"),

    // 기타 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,  "9999", "internal server error");

    private final HttpStatus status;
    private final String resultCode;
    private final String resultMessage;
}
