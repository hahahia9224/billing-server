package com.pay.api.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PromotionRequest {

    // 프로모션 유형
    private String promotionType;

    // 프로모션 제목
    @NotNull(message = "promotionTitle is required")
    @Size(max = 255, message = "promotionTitle length is must be 255 characters or less")
    private String promotionTitle;

    // 프로모션 할인 금액
    private Integer promotionAmount;

    // 프로모션 할인 비율
    private Float promotionRatio;
}
