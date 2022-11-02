package com.app.consumer.service;

import com.app.consumer.controller.Controller;
import com.app.consumer.entity.Availability;
import com.app.consumer.entity.DoctorAvailability;
import com.app.consumer.entity.MedicalAppointment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumerService implements Runnable{

    final ObjectMapper objectMapper = new ObjectMapper();
    private List<DoctorAvailability> availabilityList;

    {
        try {
            availabilityList = objectMapper.readValue(new File("C:\\Users\\User\\Desktop\\FAF\\" +
                    "FAF-sem5\\Network programming (PR)\\Minimum Task\\Lab2\\consumer\\src\\main\\java\\" +
                    "com\\app\\consumer\\resources\\availability.json"), List.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BlockingQueue<MedicalAppointment> appointmentsQueue = Controller.getAppointmentsQueue();

    private final ReentrantLock mutex = new ReentrantLock();


    @Override
    public void run() {
        while(true) {
            try {
                mutex.tryLock();
                if(!appointmentsQueue.isEmpty()) {
                    MedicalAppointment appointment = appointmentsQueue.take();
                    String timeSlotToRemove = null;

                    for (DoctorAvailability availability : availabilityList) {
                        if (availability.getDoctorName().equals(appointment.getDoctorName())) {
                            Availability byDateAvailability =
                                    availability.getAvailability().stream().
                                            filter(e -> e.getDate().equals(appointment.getDate())).findAny().get();
                            timeSlotToRemove = byDateAvailability.getTime().stream().
                                    filter(e -> e.equals(appointment.getTime())).findAny().get();
                        }
                    }

                    // update availabilityList
                    availabilityList.stream().filter(e -> e.getDoctorName().equals(appointment.getDoctorName())).
                            findAny().get().getAvailability().stream().
                            filter(e -> e.getDate().equals(appointment.getDate())).findAny().get().
                            getTime().remove(timeSlotToRemove);

                    sendDoctorAvailability(availabilityList);
                    sendAppointmentConfirmation(appointment);

                }
                else {
                    sendDoctorAvailability(availabilityList);
                }
                mutex.unlock();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendDoctorAvailability(List<DoctorAvailability> availabilityList){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<DoctorAvailability>> requestBody = new HttpEntity<>(availabilityList);
        restTemplate.postForObject("http://localhost:8081/available", requestBody, List.class);
    }

    public void sendAppointmentConfirmation(MedicalAppointment appointment){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MedicalAppointment> requestBody = new HttpEntity<>(appointment);
        restTemplate.postForObject("http://localhost:8081/consumer", requestBody, MedicalAppointment.class);
    }
}
