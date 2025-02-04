package com.devcambo.springinit.controller;

import com.devcambo.springinit.constant.StatusCode;
import com.devcambo.springinit.model.base.APIResponse;
import com.devcambo.springinit.model.base.ErrorInfo;
import com.devcambo.springinit.model.dto.request.UserCreationDto;
import com.devcambo.springinit.model.dto.request.UserUpdateDto;
import com.devcambo.springinit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management", description = "APIs for user management")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

  private final UserService userService;

  @Operation(
    summary = "Find All Users REST API",
    description = "REST API to find all users with pagination and sorting options"
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
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
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
  @GetMapping
  public ResponseEntity<APIResponse> findAllUsers(
    @Parameter(description = "Zero-based offset index", example = "0") @RequestParam(
      value = "offset",
      required = false,
      defaultValue = "0"
    ) @Min(0) Integer offset,
    @Parameter(
      description = "Number of items per page (1-100)",
      example = "10"
    ) @RequestParam(value = "pageSize", required = false, defaultValue = "10") @Min(
      1
    ) @Max(100) Integer pageSize,
    @Parameter(
      description = "Sort by field (userId, email)",
      example = "userId"
    ) @RequestParam(
      value = "sortBy",
      required = false,
      defaultValue = "userId"
    ) String sortBy,
    @Parameter(description = "Sort direction (ASC/DESC)", example = "ASC") @RequestParam(
      value = "sortDir",
      required = false,
      defaultValue = "ASC"
    ) @Pattern(regexp = "ASC|DESC", flags = Pattern.Flag.CASE_INSENSITIVE) String sortDir
  ) {
    return ResponseEntity
      .ok()
      .cacheControl(CacheControl.maxAge(30, TimeUnit.SECONDS))
      .body(
        new APIResponse(
          true,
          StatusCode.OK,
          "Retrieved all users successfully",
          userService.readAll(offset, pageSize, sortBy, sortDir)
        )
      );
  }

  @Operation(
    summary = "Find User By UserId REST API",
    description = "REST API to find user by userId"
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
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "User not found",
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
  @GetMapping("/{userId}")
  public ResponseEntity<APIResponse> findUserById(@PathVariable Long userId) {
    return ResponseEntity
      .status(HttpStatus.FOUND)
      .body(
        new APIResponse(
          true,
          StatusCode.FOUND,
          "Retrieved user successfully",
          userService.readById(userId)
        )
      );
  }

  @Operation(
    summary = "Create User REST API",
    description = "REST API to create a new user"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "201",
        description = "HTTP Status Created",
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
        responseCode = "403",
        description = "Forbidden",
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
  @PostMapping
  public ResponseEntity<APIResponse> createUser(
    @Valid @RequestBody UserCreationDto userCreationDto
  ) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(
        new APIResponse(
          true,
          StatusCode.CREATED,
          "User created successfully",
          userService.create(userCreationDto)
        )
      );
  }

  @Operation(
    summary = "Update User REST API",
    description = "REST API to update an existing user"
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
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "User not found",
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
  @PutMapping("/{userId}")
  public ResponseEntity<APIResponse> updateUser(
    @PathVariable Long userId,
    @Valid @RequestBody UserUpdateDto userUpdateDto
  ) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(
        new APIResponse(
          true,
          StatusCode.OK,
          "User updated successfully",
          userService.update(userId, userUpdateDto)
        )
      );
  }

  @Operation(
    summary = "Delete User REST API",
    description = "REST API to delete an existing user"
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
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorInfo.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "User not found",
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
  @DeleteMapping("/{userId}")
  public ResponseEntity<APIResponse> deleteUser(@PathVariable Long userId) {
    userService.delete(userId);
    return ResponseEntity
      .status(HttpStatus.NO_CONTENT)
      .body(new APIResponse(true, StatusCode.NO_CONTENT, "User deleted successfully"));
  }
}
