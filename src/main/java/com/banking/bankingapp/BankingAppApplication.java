package com.banking.bankingapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.io.IOException;

@SpringBootApplication
@EnableJpaAuditing
public class BankingAppApplication {


	@Bean
	EmbeddedKafkaBroker broker() {
		return new EmbeddedKafkaBroker(1)
				.kafkaPorts(9092)
				.brokerListProperty("localhost");
	}

	public static void main(String[] args) {
		SpringApplication.run(BankingAppApplication.class, args);
	}

}
