package com.devcambo.springinit.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
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
    @Server(url = "https://monkfish-app-a3pbe.ondigitalocean.app", description = "Staging server"),
    @Server(url = "https://example.com", description = "Production server"),
  }
)
public class SwaggerConfig {}
