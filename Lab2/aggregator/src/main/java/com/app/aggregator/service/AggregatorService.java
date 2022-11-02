package com.app.aggregator.service;

import com.app.aggregator.controller.Controller;
import com.app.aggregator.entity.MedicalAppointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
public class AggregatorService implements Runnable{

    private BlockingQueue<MedicalAppointment> producerQueue = Controller.getProducerQueue();
    private BlockingQueue<MedicalAppointment> consumerQueue = Controller.getProducerQueue();

    private final ReentrantLock mutex = new ReentrantLock();

    @Override
    public void run() {

        while (true) {
            try {
                mutex.tryLock();

                sendToConsumer(producerQueue.take());
                log.info("Sending appointment request to the consumer (hospital management system)");

                sendToProducer(consumerQueue.take());
                log.info("Sending appointment confirmation to the producer (patient)");

                mutex.unlock(); // releasing lock for other threads
                Thread.sleep(2000);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }


    public void sendToProducer(MedicalAppointment appointment){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MedicalAppointment> requestBody = new HttpEntity<>(appointment);
        restTemplate.postForObject("http://localhost:8080/confirmation", requestBody, MedicalAppointment.class);
    }


    public void sendToConsumer(MedicalAppointment appointment){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MedicalAppointment> requestBody = new HttpEntity<>(appointment);
        restTemplate.postForObject("http://localhost:8082/appointment", requestBody, MedicalAppointment.class);
    }
}
