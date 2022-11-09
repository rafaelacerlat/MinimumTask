package com.app.aggregator.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "time"
})

@Setter
@Getter
@ToString
public class Availability {

    @JsonProperty("date")
    private String date;
    @JsonProperty("time")
    private List<String> time = null;
}
