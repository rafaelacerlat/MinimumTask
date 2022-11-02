package com.app.aggregator.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
public class MedicalAppointment {
    private Patient patient;
    private String doctorName;
    private String date;
    private String time;

}
