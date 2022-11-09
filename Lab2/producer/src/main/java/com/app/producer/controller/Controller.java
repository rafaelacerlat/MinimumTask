package com.app.producer.controller;

import com.app.producer.entity.MedicalAppointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Controller {

    @PostMapping("/confirmation")
    public void post(@RequestBody MedicalAppointment appointment) {
        log.info("Received the medical appointment confirmation from aggregator! " +
                "\n The appointment has the following details: {} ", appointment.toString());
    }

}
