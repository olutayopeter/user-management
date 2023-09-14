package com.fastcredit.usermanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI apiInfo()
	{
	     return new OpenAPI()

				 .info(new Info().title("USER MANAGEMENT SERVICE API")
						 .description("API Integration For User Management Service")
						 .version("v1"));
	}
}
