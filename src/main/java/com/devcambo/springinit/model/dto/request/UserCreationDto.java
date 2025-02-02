package com.devcambo.springinit.model.dto.request;

import com.devcambo.springinit.model.enums.Gender;
import com.devcambo.springinit.validation.UniqueEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(name = "UserCreationDto", description = "User creation request schema")
public record UserCreationDto(
  @NotEmpty(message = "Username is required!")
  @Schema(description = "Username", example = "johndoe")
  String username,
  @NotEmpty(message = "Email is required!")
  @Email(message = "Invalid email address!")
  @UniqueEmail
  @Schema(description = "Email address", example = "example@example.com")
  String email,
  @NotEmpty(message = "Password is required!")
  @Schema(description = "Password", example = "password123")
  String password,
  @NotNull(message = "Gender is required!")
  @Schema(
    description = "Gender",
    example = "MALE",
    allowableValues = { "MALE", "FEMALE", "OTHER" }
  )
  Gender gender
) {}
