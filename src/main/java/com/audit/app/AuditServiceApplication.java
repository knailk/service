package com.audit.app;

import java.util.TimeZone;

import javax.annotation.PostConstruct;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@ComponentScan({ "com.audit.controller", "com.audit.service", "com.audit.converter", "com.audit.exception" })
@EntityScan("com.audit.entity")
@EnableJpaRepositories("com.audit.repository")
@EnableFeignClients("com.audit.feignclients")
@EnableEurekaClient
public class AuditServiceApplication {

	@PostConstruct
	public void init() {
		// Setting Spring Boot SetTimeZone
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(AuditServiceApplication.class, args);
	}

}
