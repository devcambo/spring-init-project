package com.devcambo.springinit.model.dto.response;

import com.devcambo.springinit.model.enums.Gender;

public record UserResponseDto(
        Long userId,
        String email,
        Gender gender
) {
}
