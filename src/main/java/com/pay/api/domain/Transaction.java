package com.pay.api.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {

    @Id
    @Column(name = "tr_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_seq")
    private Long accountSeq;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "payment_amount")
    private Long paymentAmount;

    @Column(name = "title")
    private String title;

    @Column(name = "registered")
    private LocalDateTime registeredDate;

}
