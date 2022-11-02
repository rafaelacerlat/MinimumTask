package com.app.aggregator.controller;

import com.app.aggregator.entity.DoctorAvailability;
import com.app.aggregator.entity.MedicalAppointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@RestController
public class Controller {

    static BlockingQueue<MedicalAppointment> producerQueue = new LinkedBlockingQueue<>();
    static BlockingQueue<MedicalAppointment> consumerQueue = new LinkedBlockingQueue<>();
    static  List<DoctorAvailability> doctorAvailability;


    public static BlockingQueue<MedicalAppointment> getProducerQueue(){
        return producerQueue;
    }

    public static BlockingQueue<MedicalAppointment> getConsumerQueue(){ return consumerQueue; }

    @GetMapping("/available")
    public ResponseEntity<List<DoctorAvailability>> getAvailability(){
        return new ResponseEntity<>(doctorAvailability, HttpStatus.OK);
    }

    @PostMapping("/available")
    public void postAvailability(@RequestBody List<DoctorAvailability> doctorAvailability ) {

        this.doctorAvailability = doctorAvailability;
        log.info("Received the doctors availability schedule from consumer (hospital management system)");
    }

        @PostMapping("/producer")
    public void postAppointmentRequest(@RequestBody MedicalAppointment appointment) throws InterruptedException {

        producerQueue.put(appointment);
        log.info("Received the medical appointment request! ");
    }

    @PostMapping("/consumer")
    public void postAppointmentConfirmation(@RequestBody MedicalAppointment appointment) throws InterruptedException {

        consumerQueue.put(appointment);
        log.info("Received the medical appointment confirmation! ");
    }


}
