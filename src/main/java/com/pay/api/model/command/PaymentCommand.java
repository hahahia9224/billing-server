package com.pay.api.model.command;

import com.pay.api.controller.request.PaymentRequest;
import com.pay.api.controller.request.PromotionRequest;
import com.pay.api.exception.InvalidParameterException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PaymentCommand {

    // 프로모션 적용 전 가격
    private Integer amount;

    // 프로모션 적용 후 가격
    private Integer promotionFinalPrice;

    // 프로모션 적용 여부
    private Boolean isPromotionPrice;

    // 결제 제목
    private String title;

    // 프로모션 적용 상세
    private List<PromotionCommand> promotionCommands;

    // PaymentRequest 를 PaymentCommand 객체로 변환
    // PaymentCommand 객체로 변환 시, validate 작업 수행
    public static PaymentCommand convert(PaymentRequest paymentRequest) {
        List<PromotionRequest> promotions = paymentRequest.getPromotions();

        // 프로모션 정보 없는 경우
        // createPaymentCommandWithoutPromotion 를 통해 PaymentCommand 생성
        if (CollectionUtils.isEmpty(promotions)) {
            return createPaymentCommandWithoutPromotion(paymentRequest);
        }

        // 최대 2개의 프로모션을 받을 수 있음
        if (promotions.size() > 2) {
            throw new InvalidParameterException("Promotion count must be less than or equal to 2");
        }

        // promotionCommands 리스트 생성
        List<PromotionCommand> promotionCommands = promotions.stream()
                .map(PromotionCommand::from)
                .toList();

        // promotionCommands 데이터 검증
        validatePromotions(promotionCommands, paymentRequest.getAmount());

        // promotionCommands 를 이용하여 할인 적용 금액 계산
        int promotionFinalPrice = calculatePromotionFinalPrice(paymentRequest.getAmount(), promotionCommands);

        // 최종 PaymentCommand 객체 Return
        return new PaymentCommand(
                paymentRequest.getAmount(),
                promotionFinalPrice,
                true,
                paymentRequest.getTitle(),
                promotionCommands
        );
    }

    private static PaymentCommand createPaymentCommandWithoutPromotion(PaymentRequest paymentRequest) {
        return new PaymentCommand(
                paymentRequest.getAmount(),
                paymentRequest.getAmount(),
                false,
                paymentRequest.getTitle(),
                null
        );
    }

    private static int calculatePromotionFinalPrice(int amount, List<PromotionCommand> promotionCommands) {
        int finalPrice = amount;

        for (PromotionCommand command : promotionCommands) {
            int discountPrice = calculateDiscountPrice(amount, command);
            finalPrice -= discountPrice;

            if (finalPrice < 0) {
                throw new InvalidParameterException("The promotion final price exceeds the amount.");
            }
        }

        return finalPrice;
    }

    private static void validatePromotions(List<PromotionCommand> commands, int amount) {
        for (PromotionCommand command : commands) {
            switch (command.getPromotionType()) {
                case AMOUNT -> {
                    Integer promotionAmount = command.getPromotionAmount();
                    if (promotionAmount == null) {
                        throw new InvalidParameterException("PromotionAmount is required (if amount promotion type)");
                    }

                    // 1 이상의 amount 보다 작은 정수
                    if (promotionAmount < 1 || promotionAmount >= amount) {
                        throw new InvalidParameterException("PromotionAmount must be between 1 and the amount.");
                    }
                }
                case RATIO -> {
                    Float promotionRatio = command.getPromotionRatio();
                    if (promotionRatio == null) {
                        throw new InvalidParameterException("promotionRatio is required (if ratio promotion type)");
                    }

                    // 0 초과 100 이하의 소수
                    if (promotionRatio <= 0 || promotionRatio > 100) {
                        throw new InvalidParameterException("promotionRatio range is 0 ~ 100");
                    }
                }
            }
        }
    }

    private static int calculateDiscountPrice(int amount, PromotionCommand command) {
        return switch (command.getPromotionType()) {
            case AMOUNT -> command.getPromotionAmount();
            case RATIO -> (int) (amount * (command.getPromotionRatio() / 100));
        };
    }

}
