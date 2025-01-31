package com.devcambo.springinit.model.dto.request;

import com.devcambo.springinit.model.enums.Gender;
import com.devcambo.springinit.validation.UniqueEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/*
* TODO: handle gender validation when (gender is empty or invalid)
* */
@Schema(name = "UserRequestDto", description = "User request schema")
public record UserRequestDto(
  @NotEmpty(message = "Email is required!")
  @Email(message = "Invalid email address!")
  @UniqueEmail
  @Schema(description = "Email address", example = "example@example.com")
  String email,
  @NotNull(message = "Gender is required!")
  @Schema(
    description = "Gender",
    example = "MALE",
    allowableValues = { "MALE", "FEMALE", "OTHER" }
  )
  Gender gender
) {}
