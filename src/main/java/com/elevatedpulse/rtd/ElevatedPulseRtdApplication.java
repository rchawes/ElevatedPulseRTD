package com.elevatedpulse.rtd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElevatedPulseRtdApplication {
	public static void main(String[] args) {
		SpringApplication.run(ElevatedPulseRtdApplication.class, args);
	}
}