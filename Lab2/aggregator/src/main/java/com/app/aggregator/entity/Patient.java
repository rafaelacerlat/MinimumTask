package com.app.aggregator.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class Patient {
    UUID id;
    String name;
    int age;
    List<String> allergies;
    List<String> medication;

}
