package com.linecorp.linepay.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.linecorp.linepay.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineApiResponse {

    private final String resultCode;

    private final String resultMessage;

    private final Long transactionSeq;

    private final Integer balance;

    public static LineApiResponse errorFrom(ErrorCode errorCode, String resultMessage) {
        return new LineApiResponse(
                errorCode.getResultCode(),
                resultMessage,
                null,
                null
        );
    }

    public static LineApiResponse ok(Long transactionSeq, Integer balance) {
        return new LineApiResponse(
                "0000",
                "success",
                transactionSeq,
                balance
        );
    }
}
