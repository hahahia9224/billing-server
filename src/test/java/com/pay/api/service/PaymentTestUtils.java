package com.pay.api.service;

import com.pay.api.controller.request.PaymentRequest;
import com.pay.api.controller.request.PromotionRequest;
import com.pay.api.model.command.PaymentCommand;
import com.pay.api.model.entity.Account;
import com.pay.api.model.entity.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentTestUtils {

    public static PaymentCommand getMockPaymentCommand(Integer amount, Integer promotionFinalPrice, Boolean isPromotionPrice) {
        String title = "test_title";
        return new PaymentCommand(amount, promotionFinalPrice, isPromotionPrice, title, new ArrayList<>());
    }

    public static Account getMockAccount(Long accountId, Integer balance) {
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        mockAccount.setRegisteredDate(LocalDateTime.now());
        mockAccount.setBalance(balance);
        return mockAccount;
    }

    public static Transaction getMockTransaction(Long transactionSeq, Long accountSeq, Integer amount, Integer paymentAmount, String title) {
        Transaction mockTransaction = new Transaction();
        mockTransaction.setId(transactionSeq);
        mockTransaction.setAccountSeq(accountSeq);
        mockTransaction.setAmount(amount);
        mockTransaction.setPaymentAmount(paymentAmount);
        mockTransaction.setTitle(title);
        mockTransaction.setRegisteredDate(LocalDateTime.now());
        return mockTransaction;
    }

    public static PaymentRequest getMockPaymentRequest(Integer amount, List<PromotionRequest> promotions) {
        return new PaymentRequest(amount, "title", promotions);
    }

    public static PromotionRequest getMockPromotionRequest(String promotionType, Integer promotionAmount, Float promotionRatio) {
        return new PromotionRequest(promotionType, "promotionTitleTest", promotionAmount, promotionRatio);
    }

}
