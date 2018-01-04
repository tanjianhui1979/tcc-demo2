package com.example.tccdemo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class TccDemo2Application {

	public static void main(String[] args) {
		SpringApplication.run(TccDemo2Application.class, args);
	}
}
