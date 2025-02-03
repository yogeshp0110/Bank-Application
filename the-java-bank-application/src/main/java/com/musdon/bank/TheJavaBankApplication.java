package com.musdon.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
		
		info=@Info(
				title="The Java Bank Application",
				description="Backent Rest API's for TJA Bank",
				version="v1.0",
				contact= @Contact (
						     name="Yogesh Patil",
						     email="yogeshp@gmail.com",
						     url="https://github.com/yogeshp0110"
						),
				license=@License(
						name="Code Each Other",
						url="https://github.com/yogeshp0110"
						)
				),
		externalDocs=@ExternalDocumentation(
				description="The java bank application documention",
				url="https://github.com/yogeshp0110/javabank"
				)		
		)
public class TheJavaBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheJavaBankApplication.class, args);
	}

}
