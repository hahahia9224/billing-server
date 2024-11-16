package com.pay.api.helper;

import com.pay.api.exception.InvalidParameterException;
import com.pay.api.controller.request.PaymentRequest;
import com.pay.api.controller.request.PromotionRequest;
import com.pay.api.model.command.PaymentCommand;
import com.pay.api.model.command.PromotionCommand;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class PaymentCommandConverter {
    public PaymentCommand convert(PaymentRequest paymentRequest) {
        List<PromotionRequest> promotions = paymentRequest.getPromotions();

        if (CollectionUtils.isEmpty(promotions)) {
            return createPaymentCommandWithoutPromotion(paymentRequest);
        }

        if (promotions.size() > 2) {
            throw new InvalidParameterException("Promotion count must be less than or equal to 2");
        }

        List<PromotionCommand> promotionCommands = promotions.stream()
                .map(PromotionCommand::from)
                .toList();

        int promotionFinalPrice = calculatePromotionFinalPrice(paymentRequest.getAmount(), promotionCommands);

        return new PaymentCommand(
                paymentRequest.getAmount(),
                promotionFinalPrice,
                true,
                paymentRequest.getTitle(),
                promotionCommands
        );
    }

    private PaymentCommand createPaymentCommandWithoutPromotion(PaymentRequest paymentRequest) {
        return new PaymentCommand(
                paymentRequest.getAmount(),
                paymentRequest.getAmount(),
                false,
                paymentRequest.getTitle(),
                null
        );
    }

    private int calculatePromotionFinalPrice(int amount, List<PromotionCommand> promotionCommands) {
        int finalPrice = amount;

        for (PromotionCommand command : promotionCommands) {
            validatePromotionType(command, amount);
            int discount = calculateDiscount(amount, command);
            finalPrice -= discount;

            if (finalPrice < 0) {
                throw new InvalidParameterException("The promotional price exceeds the amount.");
            }
        }

        return finalPrice;
    }

    private void validatePromotionType(PromotionCommand command, int amount) {
        switch (command.getPromotionType()) {
            case AMOUNT -> {
                Integer promotionAmount = command.getPromotionAmount();
                if (promotionAmount == null || promotionAmount < 1 || promotionAmount >= amount) {
                    throw new InvalidParameterException("PromotionAmount must be between 1 and the amount.");
                }
            }
            case RATIO -> {
                if (command.getPromotionRatio() == null) {
                    throw new InvalidParameterException("PromotionRatio is required for RATIO type.");
                }
            }
        }
    }

    private int calculateDiscount(int amount, PromotionCommand command) {
        return switch (command.getPromotionType()) {
            case AMOUNT -> command.getPromotionAmount();
            case RATIO -> (int) (amount * (command.getPromotionRatio() / 100));
        };
    }
}
