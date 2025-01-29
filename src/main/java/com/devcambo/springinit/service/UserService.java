package com.devcambo.springinit.service;

import com.devcambo.springinit.model.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> readAllUsers();
}
