package com.pay.api.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentResult {

    // 트랜잭션 고유값
    private final Long transactionSeq;

    // 계좌 잔액 금액
    private final Integer balance;
}
