package com.app.producer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class Patient {
    UUID id;
    String name;
    int age;
    List<String> allergies;
    List<String> medication;

}
