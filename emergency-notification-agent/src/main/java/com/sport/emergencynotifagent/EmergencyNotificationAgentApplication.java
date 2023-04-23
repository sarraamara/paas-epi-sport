package com.sport.emergencynotifagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sport.common.model"})
public class EmergencyNotificationAgentApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmergencyNotificationAgentApplication.class, args);
	}
}
