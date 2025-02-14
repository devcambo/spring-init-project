package com.devcambo.springinit.service;

import com.devcambo.springinit.model.dto.request.LoginRequestDto;
import com.devcambo.springinit.model.dto.request.RegisterDto;
import com.devcambo.springinit.model.dto.response.LoginResponseDto;

public interface AuthService {
  /**
   * Authenticates a user.
   *
   * @param loginRequestDto The LoginRequestDto object containing the user's
   *                        email and password.
   * @return The LoginResponseDto object containing the JWT token.
   */
  LoginResponseDto login(LoginRequestDto loginRequestDto);

  LoginResponseDto register(RegisterDto registerDto);
}
