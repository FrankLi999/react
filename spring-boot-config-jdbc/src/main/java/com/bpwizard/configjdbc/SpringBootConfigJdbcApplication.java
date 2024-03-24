package com.bpwizard.configjdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class SpringBootConfigJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootConfigJdbcApplication.class, args);
	}

}
