package com.app.consumer.entity;

import com.app.consumer.controller.ConsumerController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Consumer implements Runnable{

    private BlockingQueue<Bill> queue = ConsumerController.getQueue();
    private final ReentrantLock mutex = new ReentrantLock();


    @Override
    public void run() {
        while (true) {
            try {
                mutex.tryLock();
                Bill bill = queue.take();

                PaidBill paidBill = modifyData(bill);
                sendData(paidBill);

                log.info("Paid the new bill: {}", paidBill);

                mutex.unlock(); // releasing lock for other threads
                Thread.sleep(2000);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private PaidBill modifyData(Bill bill) {
        PaidBill paidBill = new PaidBill();

        BeanUtils.copyProperties(bill, paidBill);

        //set transaction Id
        paidBill.setTransactionId(ThreadLocalRandom.current().nextLong());

        return paidBill;
    }

    public void sendData(PaidBill bill){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<PaidBill> requestBody = new HttpEntity<>(bill);
        restTemplate.postForObject("http://localhost:8080/producer", requestBody, PaidBill.class);
    }
}
