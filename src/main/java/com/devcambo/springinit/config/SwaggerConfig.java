package com.devcambo.springinit.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
  info = @Info(
    title = "Spring Init API Documentation",
    description = "Spring Init API Documentation",
    version = "v1",
    contact = @Contact(
      name = "Your Name",
      email = "your@email.com",
      url = "https://www.yourwebsite.com"
    ),
    license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")
  ),
  externalDocs = @ExternalDocumentation(
    description = "Spring Init API Documentation",
    url = "https://www.yourwebsite.com/swagger-ui.html"
  ),
  servers = {
    @Server(url = "http://localhost:8080", description = "Development server"),
    @Server(url = "https://staging.devcambo.com", description = "Staging server"),
    @Server(url = "https://api.devcambo.com", description = "Production server"),
  },
  security = { @SecurityRequirement(name = "bearerAuth") }
)
@SecurityScheme(
  name = "bearerAuth",
  description = "JSON Web Token (JWT) authentication scheme",
  scheme = "bearer",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {}
