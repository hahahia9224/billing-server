package com.linecorp.linepay.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionRequest {

    // 프로모션 유형
    private String promotionType;

    // 프로모션 제목
    private String promotionTitle;

    // 프로모션 할인 금액
    private Integer promotionAmount;

    // 프로모션 할인 비율
    private Float promotionRatio;
}
