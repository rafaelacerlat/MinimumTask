package com.app.aggregator.controller;

import com.app.consumer.entity.Bill;
import com.app.consumer.entity.PaidBill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@RestController
public class Controller {

    static BlockingQueue<Bill> producerQueue = new LinkedBlockingQueue<>();
    static BlockingQueue<PaidBill> consumerQueue = new LinkedBlockingQueue<>();


    public static BlockingQueue<Bill> getProducerQueue(){
        return producerQueue;
    }

    public static BlockingQueue<PaidBill> getConsumerQueue(){ return consumerQueue; }


    @PostMapping("/producer")
    public void postAppointmentRequest(@RequestBody Bill bill) throws InterruptedException {

        producerQueue.put(bill);
        log.info("Received the payment request! ");
    }

    @PostMapping("/consumer")
    public void postAppointmentConfirmation(@RequestBody PaidBill paidBill) throws InterruptedException {

        consumerQueue.put(paidBill);
        log.info("Received the payment confirmation! ");
    }


}
