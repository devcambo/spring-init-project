package com.devcambo.springinit.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "LoginRequestDto", description = "Login request schema")
public record LoginRequestDto(
  @NotEmpty(message = "Email is required!")
  @Schema(description = "Email", example = "example@example.com")
  String email,
  @NotEmpty(message = "Password is required!")
  @Schema(description = "Password", example = "password")
  String password
) {}
