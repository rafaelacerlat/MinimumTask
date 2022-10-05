package com.app.producer.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Producer implements Runnable {

    private final ReentrantLock mutex = new ReentrantLock();

    @Override
    public void run() {
        while(true){
            try{
                Bill bill = generateData();
                sendData(bill);

                log.info("Generated new {}", bill);
                Thread.sleep(2000);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Bill generateData() throws IOException {
        //set id
        int id = ThreadLocalRandom.current().nextInt(1000,100000);

        //set serviceProvider
        List<String> list = Files.readAllLines(
                new File("C:\\Users\\User\\Desktop\\FAF\\FAF-sem5\\Network programming (PR)\\Minimum Task\\producer\\src\\main\\java\\com\\app\\producer\\resources\\serviceProviders.txt").toPath(), Charset.defaultCharset());
        String serviceProvider = list.get(ThreadLocalRandom.current().nextInt(list.size()));

        //set random date from current month
        long start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).toEpochDay();
        long end = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).toEpochDay();

        long randomDay = ThreadLocalRandom.current().nextLong(start, end);
        LocalDate date = LocalDate.ofEpochDay(randomDay);

        //set amount
        double amount = ThreadLocalRandom.current().nextDouble(1000);

        return new Bill(id, serviceProvider, date, amount) ;
    }

    public void sendData(Bill bill){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Bill> requestBody = new HttpEntity<>(bill);
        restTemplate.postForObject("http://localhost:8081/consumer", requestBody, Bill.class);
    }
}
