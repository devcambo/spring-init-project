package com.devcambo.springinit.service.impl;

import com.devcambo.springinit.mapper.UserMapper;
import com.devcambo.springinit.model.dto.request.LoginRequestDto;
import com.devcambo.springinit.model.dto.request.RegisterDto;
import com.devcambo.springinit.model.dto.response.LoginResponseDto;
import com.devcambo.springinit.model.entity.User;
import com.devcambo.springinit.repo.UserRepo;
import com.devcambo.springinit.service.AuthService;
import com.devcambo.springinit.service.JwtService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepo userRepo;
  private final UserMapper userMapper;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  @Override
  public LoginResponseDto login(LoginRequestDto loginRequestDto) {
    log.info("Login request: {}", loginRequestDto);
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginRequestDto.email(),
        loginRequestDto.password()
      )
    );
    log.info("Authentication: {}", authentication);
    String token;
    String authorities;
    authorities =
      authentication
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));
    token = jwtService.generateToken(loginRequestDto.email(), authorities);
    return new LoginResponseDto(token);
  }

  @Override
  public LoginResponseDto register(RegisterDto registerDto) {
    User user = userMapper.toEntity(registerDto);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles("ADMIN,USER,SYSTEM");
    User registeredUser = userRepo.save(user);
    String token = "";
    token =
      jwtService.generateToken(registeredUser.getEmail(), registeredUser.getRoles());
    return new LoginResponseDto(token);
  }
}
