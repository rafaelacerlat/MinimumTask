package com.app.consumer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
public class DoctorAvailability {
    @JsonProperty("doctorName")
    private String doctorName;
    @JsonProperty("availability")
    private List<Availability> availability = null;
}
