package com.devcambo.springinit.model.dto.request;

import com.devcambo.springinit.model.enums.Gender;

public record UserRequest(
    String email,
    Gender gender
) {
}
