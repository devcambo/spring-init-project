package com.devcambo.springinit.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDto(@JsonProperty("jwt_token") String token) {}
