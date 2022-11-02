package com.app.producer.entity;


import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class DoctorAvailability {
    private String doctorName;
    private List<Availability> availability;
}
