package com.app.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class Bill {
    private int id;
    private String serviceProvider;
    private LocalDate date;
    private double amount;

}
