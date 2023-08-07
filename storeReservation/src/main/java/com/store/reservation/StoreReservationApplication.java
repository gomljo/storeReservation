package com.store.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class StoreReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreReservationApplication.class, args);
	}

}
