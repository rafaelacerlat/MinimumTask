package com.app.producer.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@ToString
public class PaidBill {
    private int id;
    private String serviceProvider;
    private LocalDate date;
    private double amount;
    private Long transactionId;
}
