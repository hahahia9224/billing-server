package com.pay.api.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PaymentCommand {

    // 프로모션 적용 전 가격
    private Long amount;

    // 프로모션 적용 가격
    private Long promotionFinalPrice;

}
