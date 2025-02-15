package com.devcambo.springinit.controller;

import com.devcambo.springinit.constant.StatusCode;
import com.devcambo.springinit.model.base.APIResponse;
import com.devcambo.springinit.model.base.ErrorInfo;
import com.devcambo.springinit.model.dto.request.LoginRequestDto;
import com.devcambo.springinit.model.dto.request.RegisterDto;
import com.devcambo.springinit.model.dto.response.LoginResponseDto;
import com.devcambo.springinit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Management", description = "APIs for auth management")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

  private final AuthService authService;

  @Operation(
    summary = "Register User REST API",
    description = "REST API to register a new user"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "HTTP Status OK",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = APIResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Invalid request parameters or body",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "405",
        description = "This http method is not allowed for this API endpoint",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "500",
        description = "HTTP Status Internal Server Error",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
    }
  )
  @PostMapping("/register")
  public ResponseEntity<APIResponse> register(
    @Valid @RequestBody RegisterDto registerDto
  ) {
    LoginResponseDto loginResponseDto = authService.register(registerDto);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(
        new APIResponse(true, StatusCode.OK, "Register successfully", loginResponseDto)
      );
  }

  @Operation(summary = "Login REST API", description = "REST API to login a user")
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "HTTP Status OK",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = APIResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Invalid request parameters or body",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "405",
        description = "Method Not Allowed",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
    }
  )
  @PostMapping("/login")
  public ResponseEntity<APIResponse> login(
    @Valid @RequestBody LoginRequestDto loginRequestDto
  ) {
    LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(new APIResponse(true, StatusCode.OK, "Login successfully", loginResponseDto));
  }
}
