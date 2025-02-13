package com.devcambo.springinit.security;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
