package com.app.consumer;

import com.app.consumer.entity.Availability;
import com.app.consumer.entity.DoctorAvailability;
import com.app.consumer.entity.MedicalAppointment;
import com.app.consumer.service.ConsumerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class ConsumerApplication {
    static List<DoctorAvailability> availabilityList;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ConsumerApplication.class, args);

        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            availabilityList = objectMapper.readValue(new File("C:\\Users\\User\\Desktop\\FAF\\" +
                    "FAF-sem5\\Network programming (PR)\\Minimum Task\\Lab2\\consumer\\src\\main\\java\\" +
                    "com\\app\\consumer\\resources\\availability.json"), new TypeReference<> () {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for (int i = 0; i < 6; i++) {
            new Thread(new ConsumerService()).start();
        }
    }

    public static List<DoctorAvailability> getAvailabilityList(){
        return availabilityList;
    }

    public static void deleteTimeSlot(MedicalAppointment appointment, String timeSlotToRemove){
        availabilityList.stream().filter(e -> e.getDoctorName().equals(appointment.getDoctorName())).
                findAny().get().getAvailability().stream().
                filter(e -> e.getDate().equals(appointment.getDate())).findAny().get().
                getTime().remove(timeSlotToRemove);
    }

    public static void deleteDate(MedicalAppointment appointment, String date){
        availabilityList.stream().filter(e -> e.getDoctorName().equals(appointment.getDoctorName())).
                findAny().get().getAvailability().removeIf(e -> e.getDate().equals(date));
    }

    public static void deleteDoctor(String doctorName){
        availabilityList.removeIf(e -> e.getDoctorName().equals(doctorName));
    }

}
