package com.umpisa.restaurant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for Restaurant Reservation System.
 * Provides interactive API documentation at /swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI restaurantReservationOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");

        Contact contact = new Contact();
        contact.setName("Kervin Rey H. Balibagoso");
        contact.setEmail("kaibaltech@gmail.com");


        Info info = new Info()
                .title("Restaurant Reservation System API")
                .version("1.0.0")
                .contact(contact)
                .description("RESTful API for managing restaurant reservations with multi-channel notifications. " +
                        "This system allows customers to create, view, update, and cancel reservations, " +
                        "with automatic email/SMS notifications sent via a pub-sub event architecture.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
