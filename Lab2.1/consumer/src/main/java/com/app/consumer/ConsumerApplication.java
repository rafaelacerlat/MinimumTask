package com.app.consumer;


import com.app.consumer.service.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);

		for (int i = 0; i < 6 ; i++){
			new Thread(new Consumer()).start();
		}
	}

}
