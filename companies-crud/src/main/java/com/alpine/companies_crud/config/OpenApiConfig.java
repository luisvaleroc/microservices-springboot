package com.alpine.companies_crud.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info= @Info(
                title = "companies CRUD",
                version = "1.0.0",
                description = "This a CRUD for managment companies"
        )
)
public class OpenApiConfig {

}
