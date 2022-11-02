package com.app.aggregator;

import com.app.aggregator.service.AggregatorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AggregatorApplication.class, args);

		for (int i = 0; i < 6 ; i++){
			new Thread(new AggregatorService()).start();
		}
	}

}
