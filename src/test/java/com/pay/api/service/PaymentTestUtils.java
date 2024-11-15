package com.pay.api.service;

import com.pay.api.domain.Account;
import com.pay.api.domain.Transaction;
import com.pay.api.model.command.PaymentCommand;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

}
