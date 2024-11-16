package com.pay.api.service;

import com.pay.api.model.command.PaymentCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.pay.api.service.PaymentTestUtils.getMockPaymentCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PaymentServiceConcurrencyTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void test_concurrent_payments_count() throws InterruptedException {

        // given
        final int totalRequests = 200;
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();
        ExecutorService executor = Executors.newFixedThreadPool(totalRequests);

        Integer amount = 20000;
        Integer promotionFinalPrice = 20000;
        Boolean isPromotionPrice = true;
        Long accountSeq = 1L;
        Integer exceptedSuccessCount = 50;
        Integer exceptedFailureCount = totalRequests - exceptedSuccessCount;

        // when (concurrency paymentService.payment method)
        // threadPool 기반으로 paymentService.payment 호출 후,
        // 최종적으로 성공한 카운트 확인을 통하여 동시성 처리 확인
        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                try {
                    PaymentCommand command = getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);
                    paymentService.payment(accountSeq, command);
                    successCount.incrementAndGet();
                    System.out.println("Payment Success");
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    System.out.println("Exception: " + e.getMessage());
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // then
        assertEquals(exceptedSuccessCount, successCount.get());
        assertEquals(exceptedFailureCount, failureCount.get());
    }

}
