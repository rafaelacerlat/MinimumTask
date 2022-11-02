package com.app.consumer.controller;

import com.app.consumer.ConsumerApplication;
import com.app.consumer.entity.MedicalAppointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Slf4j
@RestController
public class Controller {

    static BlockingQueue<MedicalAppointment> appointmentsQueue = new LinkedBlockingQueue<>();

    public static BlockingQueue<MedicalAppointment> getAppointmentsQueue(){
        return appointmentsQueue;
    }

    @PostMapping("/appointment")
    public void post(@RequestBody MedicalAppointment appointment) throws InterruptedException {

        appointmentsQueue.put(appointment);
        log.info("Received the request to consumer! The medical appointment was made for:",
                appointment.getDate(), appointment.getTime());
    }


}
