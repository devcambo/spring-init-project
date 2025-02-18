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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginRequestDto.email(),
        loginRequestDto.password()
      )
    );
    String authorities = authentication
      .getAuthorities()
      .stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.joining(","));
    return new LoginResponseDto(
      jwtService.generateToken(loginRequestDto.email(), authorities)
    );
  }

  @Override
  public LoginResponseDto register(RegisterDto registerDto) {
    User user = userMapper.toEntity(registerDto);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles("SUPER_ADMIN,ADMIN,USER,SYSTEM"); // TODO: Set roles for register user by system only USER, or sth but not full authority
    User registeredUser = userRepo.save(user);
    return new LoginResponseDto(
      jwtService.generateToken(registeredUser.getEmail(), registeredUser.getRoles())
    );
  }
}
