package com.pay.api.service;

import com.pay.api.model.entity.Account;
import com.pay.api.model.entity.Transaction;
import com.pay.api.exception.AccountNotFoundException;
import com.pay.api.exception.AmountNotEnoughException;
import com.pay.api.model.command.PaymentCommand;
import com.pay.api.model.dto.PaymentResultDto;
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

        Account accountInDb = accountRepository.findByIdWithLock(accountId)
                .orElseThrow(AccountNotFoundException::new);

        if (paymentCommand.getPromotionFinalPrice() > accountInDb.getBalance()) {
            throw new AmountNotEnoughException();
        }

        Transaction transaction = new Transaction();
        transaction.setAccountSeq(accountInDb.getId());
        transaction.setAmount(paymentCommand.getAmount());
        transaction.setPaymentAmount(paymentCommand.getPromotionFinalPrice());
        transaction.setTitle(paymentCommand.getTitle());
        transaction.setRegisteredDate(LocalDateTime.now());

        accountInDb.setBalance(accountInDb.getBalance() - paymentCommand.getPromotionFinalPrice());

        Transaction transactionResult = transactionRepository.save(transaction);
        accountRepository.save(accountInDb);

        return new PaymentResultDto(transactionResult.getId(), accountInDb.getBalance());
    }
}
