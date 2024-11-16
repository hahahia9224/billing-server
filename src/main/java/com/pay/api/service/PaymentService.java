package com.pay.api.service;

import com.pay.api.exception.AccountNotFoundException;
import com.pay.api.exception.AmountNotEnoughException;
import com.pay.api.model.command.PaymentCommand;
import com.pay.api.model.dto.PaymentResultDto;
import com.pay.api.model.entity.Account;
import com.pay.api.model.entity.Transaction;
import com.pay.api.repository.AccountRepository;
import com.pay.api.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class PaymentService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public PaymentResultDto payment(Long accountId, PaymentCommand paymentCommand) {

        // STEP 1. 계좌 정보 조회 (없으면 AccountNotFoundException)
        // findByIdWithLock 을 통해 account row lock 처리
        Account accountInDb = accountRepository.findByIdWithLock(accountId)
                .orElseThrow(AccountNotFoundException::new);

        // STEP 2. 현재 계좌 잔액 기준으로 최종 결제액보다 적으면 AmountNotEnoughException 처리
        if (paymentCommand.getPromotionFinalPrice() > accountInDb.getBalance()) {
            throw new AmountNotEnoughException();
        }

        // STEP 3. 계좌 잔액 조건 충족 후, 거래 정보 생성
        Transaction transaction = new Transaction();
        transaction.setAccountSeq(accountInDb.getId());
        transaction.setAmount(paymentCommand.getAmount());
        transaction.setPaymentAmount(paymentCommand.getPromotionFinalPrice());
        transaction.setTitle(paymentCommand.getTitle());
        transaction.setRegisteredDate(LocalDateTime.now());

        // STEP 4. 계좌 데이터의 잔액 수정
        accountInDb.setBalance(accountInDb.getBalance() - paymentCommand.getPromotionFinalPrice());

        // STEP 5. 거래 정보 디비 저장 / 계좌 잔액 업데이트
        Transaction transactionResult = transactionRepository.save(transaction);
        accountRepository.save(accountInDb);

        // STEP 6. 정상적으로 수행 완료 후, transactionSeq / balance return
        return new PaymentResultDto(transactionResult.getId(), accountInDb.getBalance());
    }
}
