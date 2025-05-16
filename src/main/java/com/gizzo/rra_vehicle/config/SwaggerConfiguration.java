package com.gizzo.rra_vehicle.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Vehicle Management",
                        email = "giselemigisha53@gmail.com",
                        url = "https://www.eucl.com"
                ),
                description = " backend apis for vehicle management",
                title = "Vehicle management apis",
                version = "1.0",
                license = @License(
                        name = "Vehicle Management",
                        url = "https://www.vehiclemanager.com"
                ),
                termsOfService = "Feel free to clone and adapt this code for your own Spring Boot 3 project."
        ), servers = {
        @Server(
                description = "Local deployment  environment",
                url = "http://localhost:9000"
        ),
}
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Secure your API with a JWT  token  for user authorization",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfiguration {
}
