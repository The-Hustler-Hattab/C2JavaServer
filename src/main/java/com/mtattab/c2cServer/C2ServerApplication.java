package com.mtattab.c2cServer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;



@SpringBootApplication
@OpenAPIDefinition
@EnableWebMvc
@ComponentScan
public class C2ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(C2ServerApplication.class, args);
	}

}
