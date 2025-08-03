package com.yourcompany.workforcemgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkforcemgmtApplication {

	public static void main(String[] args) {
		System.out.println("Starting Spring Boot application...");
		SpringApplication.run(WorkforcemgmtApplication.class, args);
		System.out.println("Spring Boot application started!");	}

}
