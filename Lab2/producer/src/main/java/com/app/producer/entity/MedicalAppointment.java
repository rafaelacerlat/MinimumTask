package com.app.producer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class MedicalAppointment {
    private UUID appointmentID;
    private Patient patient;
    private String doctorName;
    private String date;
    private String time;

}
