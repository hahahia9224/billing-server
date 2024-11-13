package com.linecorp.linepay.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionRequest {

    private String promotionType;

    private String promotionTitle;

    private Integer promotionAmount;

    private Float promotionRatio;
}
