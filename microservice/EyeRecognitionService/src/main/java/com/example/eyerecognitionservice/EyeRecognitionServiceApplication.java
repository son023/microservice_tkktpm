package com.example.eyerecognitionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EyeRecognitionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EyeRecognitionServiceApplication.class, args);
	}

}
