package com.devcambo.springinit.service.impl;

import com.devcambo.springinit.model.dto.response.UserResponseDto;
import com.devcambo.springinit.repo.UserRepo;
import com.devcambo.springinit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public List<UserResponseDto> readAllUsers() {
        return null;
    }
}
