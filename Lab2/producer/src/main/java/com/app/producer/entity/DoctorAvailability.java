package com.app.producer.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class DoctorAvailability {
    private String doctorName;
    private List<Availability> availability;
}
