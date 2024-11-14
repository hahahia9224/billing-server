package com.pay.api.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PaymentCommand {

    // 프로모션 적용 전 가격
    private Integer amount;

    // 프로모션 적용 후 가격
    private Integer promotionFinalPrice;

    // 프로모션 적용 여부
    private Boolean isPromotionPrice;

    // 결제 제목
    private String title;

}
