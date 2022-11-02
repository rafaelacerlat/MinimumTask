package com.app.consumer;

import com.app.consumer.entity.DoctorAvailability;
import com.app.consumer.service.ConsumerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ConsumerApplication {
    static List<DoctorAvailability> availabilityList;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ConsumerApplication.class, args);

        for (int i = 0; i < 6; i++) {
            new Thread(new ConsumerService()).start();
        }
    }

}
