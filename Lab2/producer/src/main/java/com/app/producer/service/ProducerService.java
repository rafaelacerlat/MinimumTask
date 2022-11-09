package com.app.producer.service;


import com.app.producer.entity.Availability;
import com.app.producer.entity.DoctorAvailability;
import com.app.producer.entity.MedicalAppointment;
import com.app.producer.entity.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
public class ProducerService implements Runnable {

    private final ReentrantLock mutex = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            try {
                mutex.tryLock();

                List<DoctorAvailability> availabilityList = getAvailable();

                if (availabilityList != null) {
                    log.info("Doctors' Availability list: {}", availabilityList.toString());

                    MedicalAppointment appointment = generateAppointment(generatePatient(), availabilityList);

                    sendAppointmentRequest(appointment);

                    log.info("Sent a medical appointment request");
                }else {
                    log.info("There are no available doctors!");
                }
                mutex.unlock();
                Thread.sleep(5000);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private List<DoctorAvailability> getAvailable() {

        RestTemplate restTemplate = new RestTemplate();
        DoctorAvailability[] doctorAvailability =
                restTemplate.getForObject("http://localhost:8081/available", DoctorAvailability[].class);

        List<DoctorAvailability> doctorAvailabilityList = Arrays.asList(doctorAvailability);

        return doctorAvailabilityList;
    }

    private Patient generatePatient() {
        // set id
        UUID id = UUID.randomUUID();

        // set name
        String name = "";

        // set age
        int age = ThreadLocalRandom.current().nextInt(100);

        // set allergies
        List<String> allergies = new ArrayList<>();

        // set medication
        List<String> medication = new ArrayList<>();

        return new Patient(id, name, age, allergies, medication);
    }

    private MedicalAppointment generateAppointment(Patient patient, List<DoctorAvailability> availabilityList) {
        // select from availabilityList the needed doctor and time

        // random doctor name
        List<String> doctorNames = availabilityList.stream().map(e -> e.getDoctorName()).collect(Collectors.toList());
        int i = ThreadLocalRandom.current().nextInt(doctorNames.size());
        String doctorName = doctorNames.get(i);

        // random date and time from those available
        List<Availability> availability = availabilityList.stream().filter(e -> e.getDoctorName().equals(doctorName)).
                findAny().get().getAvailability();

        List<String> dates = availability.stream().map(e -> e.getDate()).collect(Collectors.toList());

        int j = ThreadLocalRandom.current().nextInt(dates.size());
        String date = dates.get(j);

        List<String> timeSlots = availability.stream().filter(e -> e.getDate().equals(date)).findAny().get().getTime();

        int k = ThreadLocalRandom.current().nextInt(timeSlots.size());
        String time = timeSlots.get(k);


        return new MedicalAppointment(UUID.randomUUID(), patient,
                doctorName, date, time);
    }

    private void sendAppointmentRequest(MedicalAppointment appointment) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MedicalAppointment> requestBody = new HttpEntity<>(appointment);
        restTemplate.postForObject("http://localhost:8081/producer", requestBody, MedicalAppointment.class);
    }
}
