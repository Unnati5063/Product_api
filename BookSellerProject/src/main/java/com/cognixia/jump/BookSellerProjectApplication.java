package com.cognixia.jump;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@SecurityScheme(name = "v3/api-docs", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(
		info = @Info( title="BookSeller", version="1.0",
				description = "Allow user to buy books in the application.",
				contact = @Contact(name = "Unnati", email = "abc@gmail.com",
									url = "mybook.com")))
public class BookSellerProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookSellerProjectApplication.class, args);
	}

}