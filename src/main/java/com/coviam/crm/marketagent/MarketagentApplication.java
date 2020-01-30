package com.coviam.crm.marketagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableFeignClients
public class MarketagentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketagentApplication.class, args);
	}

}
