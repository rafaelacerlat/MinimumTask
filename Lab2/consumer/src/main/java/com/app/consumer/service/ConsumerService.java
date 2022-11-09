package com.app.consumer.service;

import com.app.consumer.ConsumerApplication;
import com.app.consumer.controller.Controller;
import com.app.consumer.entity.Availability;
import com.app.consumer.entity.DoctorAvailability;
import com.app.consumer.entity.MedicalAppointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ConsumerService implements Runnable {


    private List<DoctorAvailability> availabilityList;
    private BlockingQueue<MedicalAppointment> appointmentsQueue = Controller.getAppointmentsQueue();

    private final ReentrantLock mutex = new ReentrantLock();


    @Override
    public void run() {
        while (true) {
            try {
                mutex.tryLock();

                availabilityList = ConsumerApplication.getAvailabilityList();
                sendDoctorAvailability(availabilityList);

                MedicalAppointment appointment = appointmentsQueue.take();
                String timeSlotToRemove = null;
                Availability byDateAvailability = null;
                DoctorAvailability doctorAvailability = null;

                for (DoctorAvailability availability : availabilityList) {
                    if (availability.getDoctorName().equals(appointment.getDoctorName())) {
                        doctorAvailability = availability;
                        byDateAvailability =
                                availability.getAvailability().stream().
                                        filter(e -> e.getDate().equals(appointment.getDate())).findAny().get();
                        timeSlotToRemove = byDateAvailability.getTime().stream().
                                filter(e -> e.equals(appointment.getTime())).findAny().get();
                    }
                }

                // in case this is the doctor's last availability
                if (doctorAvailability.getAvailability().size() < 2){
                    ConsumerApplication.deleteDoctor(doctorAvailability.getDoctorName());
                }
                // in case this time slot was the last for the specified date
                else if (byDateAvailability.getTime().size() < 2) {
                    ConsumerApplication.deleteDate(appointment, byDateAvailability.getDate());
                }
                else{
                    ConsumerApplication.deleteTimeSlot(appointment, timeSlotToRemove);
                }

                sendAppointmentConfirmation(appointment);

                mutex.unlock();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendDoctorAvailability(List<DoctorAvailability> availabilityList) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<DoctorAvailability>> requestBody = new HttpEntity<>(availabilityList);
        restTemplate.postForObject("http://localhost:8081/available", requestBody, List.class);
    }

    public void sendAppointmentConfirmation(MedicalAppointment appointment) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MedicalAppointment> requestBody = new HttpEntity<>(appointment);
        restTemplate.postForObject("http://localhost:8081/consumer", requestBody, MedicalAppointment.class);
    }
}
