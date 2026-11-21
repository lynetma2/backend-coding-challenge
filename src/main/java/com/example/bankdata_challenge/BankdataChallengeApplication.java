package com.example.bankdata_challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BankdataChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankdataChallengeApplication.class, args);
	}

}
