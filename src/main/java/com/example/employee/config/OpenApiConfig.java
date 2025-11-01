package com.example.employee.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("Local Development Server");

        Server productionServer = new Server();
        productionServer.setUrl("https://api.employee-management.com");
        productionServer.setDescription("Production Server");

        Contact contact = new Contact();
        contact.setEmail("support@employee-management.com");
        contact.setName("Employee Management Support Team");
        contact.setUrl("https://www.employee-management.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Employee Management API")
                .version("1.0.0")
                .contact(contact)
                .description("RESTful API for managing employee records with full CRUD operations. " +
                        "This API provides endpoints to create, read, update, and delete employee information " +
                        "including name, email, and role.")
                .termsOfService("https://www.employee-management.com/terms")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, productionServer))
                .tags(List.of(
                        new Tag().name("Employee Management")
                                .description("Operations related to employee CRUD operations")
                ));
    }
}