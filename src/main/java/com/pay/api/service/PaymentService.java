package com.pay.api.service;

import com.pay.api.domain.Account;
import com.pay.api.domain.Transaction;
import com.pay.api.exception.AccountNotFoundException;
import com.pay.api.exception.AmountNotEnoughException;
import com.pay.api.model.command.PaymentCommand;
import com.pay.api.repository.AccountRepository;
import com.pay.api.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class PaymentService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public PaymentResult payment(Long accountId, PaymentCommand paymentCommand) {

        // TODO row lock 처리 필요
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException();
        }

        Account accountInDb = account.get();

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

        return new PaymentResult(transactionResult.getId(), accountInDb.getBalance());
    }
}
