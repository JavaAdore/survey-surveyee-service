package com.survey.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableCircuitBreaker
@EnableFeignClients
@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaAuditing
@PropertySource(value = { "classpath:db.properties" ,"classpath:redis.properties"})
public class SurveyeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveyeeServiceApplication.class, args);
	}

}
