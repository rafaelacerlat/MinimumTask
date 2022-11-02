package com.app.aggregator.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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

