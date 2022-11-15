package com.app.producer;

import com.app.producer.service.Producer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);

		for (int i = 0; i < 7 ; i++){
			new Thread(new Producer()).start();
		}
	}

}
