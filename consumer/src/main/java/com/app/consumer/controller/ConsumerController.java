package com.app.consumer.controller;

import com.app.consumer.entity.Bill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@RestController
public class ConsumerController {

    static BlockingQueue<Bill> queue = new LinkedBlockingQueue<>();
    public static BlockingQueue<Bill> getQueue(){
        return queue;
    }

    @PostMapping("/consumer")
    public void post(@RequestBody Bill bill) throws InterruptedException {

        queue.put(bill);
        log.info(bill.toString());
    }
}
