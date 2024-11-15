package com.pay.api.model.command;

import com.pay.api.controller.request.PromotionRequest;
import com.pay.api.model.entity.PromotionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PromotionCommand {

    private PromotionType promotionType;

    private String promotionTitle;

    private Integer promotionAmount;

    private Float promotionRatio;

    public static PromotionCommand from(PromotionRequest promotionRequest) {
        return new PromotionCommand(
                PromotionType.from(promotionRequest.getPromotionType()),
                promotionRequest.getPromotionTitle(),
                promotionRequest.getPromotionAmount(),
                promotionRequest.getPromotionRatio()
        );
    }
}
