package com.marceldias.birthday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@ActiveProfiles("mysql")
public class TestBirthdayMessageApiApplication {

	@Bean
	@ServiceConnection
	MySQLContainer<?> mysqlContainer() {
		return new MySQLContainer<>(DockerImageName.parse("mysql:8.0.20"));
	}

	public static void main(String[] args) {
		SpringApplication.from(Application::main)
			.with(TestBirthdayMessageApiApplication.class)
			.run(args);
	}
}
