package com.worksphere.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WorksphereApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorksphereApiApplication.class, args);
	}

}
