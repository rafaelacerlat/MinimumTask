package com.app.aggregator.service;

import com.app.aggregator.controller.Controller;
import com.app.consumer.entity.Bill;
import com.app.consumer.entity.PaidBill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
public class Aggregator implements Runnable{

    private BlockingQueue<Bill> producerQueue = Controller.getProducerQueue();
    private BlockingQueue<PaidBill> consumerQueue = Controller.getConsumerQueue();

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

    public void sendToProducer(PaidBill paidBill){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<PaidBill> requestBody = new HttpEntity<>(paidBill);
        restTemplate.postForObject("http://localhost:8080/confirmation", requestBody, PaidBill.class);
    }

    public void sendToConsumer(Bill bill){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Bill> requestBody = new HttpEntity<>(bill);
        restTemplate.postForObject("http://localhost:8082/payment", requestBody, Bill.class);
    }
}

