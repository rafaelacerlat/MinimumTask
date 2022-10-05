package com.app.producer.controller;

import com.app.producer.entity.PaidBill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProducerController {

    @PostMapping("/producer")
    public void post(@RequestBody PaidBill paidBill) {

        log.info("{} successfully !", paidBill);
    }

}
