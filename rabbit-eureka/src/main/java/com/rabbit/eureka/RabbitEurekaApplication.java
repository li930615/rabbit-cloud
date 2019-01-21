package com.rabbit.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RabbitEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitEurekaApplication.class, args);
	}

}

