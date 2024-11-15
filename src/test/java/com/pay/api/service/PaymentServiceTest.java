package com.pay.api.service;

import com.pay.api.domain.Account;
import com.pay.api.domain.Transaction;
import com.pay.api.exception.AccountNotFoundException;
import com.pay.api.exception.AmountNotEnoughException;
import com.pay.api.model.command.PaymentCommand;
import com.pay.api.repository.AccountRepository;
import com.pay.api.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PaymentService paymentService;

    private final Long accountId = 1L;

    @Test
    void account_not_found_exception() {
        Integer amount = 2000;
        Integer promotionFinalPrice = 1500;
        Boolean isPromotionPrice = true;

        // given
        PaymentCommand mockPayCommand = getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);

        // when
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // then
        assertThrows(AccountNotFoundException.class, () -> paymentService.payment(accountId, mockPayCommand));
    }

    @Test
    void amount_is_more_than_account_balance_exception() {
        Integer balance = 1000;
        Integer amount = 2000;
        Integer promotionFinalPrice = 1500;
        Boolean isPromotionPrice = true;

        // given
        Account mockAccount = getMockAccount(balance);
        PaymentCommand mockPayCommand = getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);

        // when
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // then
        assertThrows(AmountNotEnoughException.class, () -> paymentService.payment(accountId, mockPayCommand));
    }

    @Test
    void promotion_amount_more_than_account_balance_exception() {
        // 프로모션 적용하더라도 잔액이 부족한 경우
        Integer balance = 1000;
        Integer amount = 2000;
        Integer promotionFinalPrice = 1500;
        Boolean isPromotionPrice = true;

        // given
        Account mockAccount = getMockAccount(balance);
        PaymentCommand mockPayCommand = getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);

        // when
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // then
        assertThrows(AmountNotEnoughException.class, () -> paymentService.payment(accountId, mockPayCommand));
    }

    @Test
    void transaction_success() {
        Integer balance = 10000;
        Integer amount = 2000;
        Integer promotionFinalPrice = 1500;
        Integer paymentResultBalance = balance - promotionFinalPrice;
        Boolean isPromotionPrice = true;
        Long transactionSeq = 1L;

        // given
        Account mockAccount = getMockAccount(balance);
        PaymentCommand mockPaymentCommand = getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);

        // when
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        when(transactionRepository.save(any())).thenReturn(getMockTransaction(transactionSeq, mockAccount.getId(), amount, promotionFinalPrice, "title"));

        PaymentResult actual = paymentService.payment(accountId, mockPaymentCommand);

        // then
        assertNotNull(actual);
        assertEquals(transactionSeq, actual.getTransactionSeq());
        assertEquals(paymentResultBalance, actual.getBalance());
    }

    private PaymentCommand getMockPaymentCommand(Integer amount, Integer promotionFinalPrice, Boolean isPromotionPrice) {
        String title = "test_title";
        return new PaymentCommand(amount, promotionFinalPrice, isPromotionPrice, title, new ArrayList<>());
    }

    private Account getMockAccount(Integer balance) {
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        mockAccount.setRegisteredDate(LocalDateTime.now());
        mockAccount.setBalance(balance);
        return mockAccount;
    }

    private Transaction getMockTransaction(Long transactionSeq, Long accountSeq, Integer amount, Integer paymentAmount, String title) {
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
