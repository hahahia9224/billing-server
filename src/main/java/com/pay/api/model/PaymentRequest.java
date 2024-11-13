package com.pay.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentRequest {

    // 결제 금액
    private Integer amount;

    // 결제 제목
    private String title;

    // 프로모션 정보 리스트
    private List<PromotionRequest> promotions;
}
