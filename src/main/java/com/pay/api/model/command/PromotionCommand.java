package com.pay.api.model.command;

import com.pay.api.model.api.PromotionRequest;
import com.pay.api.model.enums.PromotionType;
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
