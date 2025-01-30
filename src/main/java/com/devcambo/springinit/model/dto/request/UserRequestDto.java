package com.devcambo.springinit.model.dto.request;

import com.devcambo.springinit.model.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "UserRequestDto", description = "User request schema")
public record UserRequestDto(
  @NotEmpty(message = "Email is required!")
  @Email(message = "Invalid email address!")
  @Schema(description = "Email address", example = "example@example.com")
  String email,
  @NotEmpty(message = "Gender is required!")
  @Schema(description = "Gender", example = "MALE")
  Gender gender
) {}
