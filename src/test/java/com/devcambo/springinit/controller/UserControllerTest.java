package com.devcambo.springinit.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.devcambo.springinit.constant.StatusCode;
import com.devcambo.springinit.exception.InvalidSortFieldException;
import com.devcambo.springinit.exception.ResourceNotFoundException;
import com.devcambo.springinit.model.dto.request.UserCreationDto;
import com.devcambo.springinit.model.dto.request.UserUpdateDto;
import com.devcambo.springinit.model.dto.response.UserResponseDto;
import com.devcambo.springinit.model.enums.Gender;
import com.devcambo.springinit.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void testFindAllUsersSuccess() throws Exception {
    // Given
    UserResponseDto u1 = new UserResponseDto(
      1L,
      "Albus Dumbledore",
      "7sCm6@example.com",
      Gender.MALE
    );
    UserResponseDto u2 = new UserResponseDto(
      2L,
      "Harry Potter",
      "7sCm6@example.com",
      Gender.MALE
    );
    List<UserResponseDto> users = Arrays.asList(u1, u2);
    Page<UserResponseDto> usersPage = new PageImpl<>(users);
    given(userService.readAll(0, 10, "userId", "ASC")).willReturn(usersPage);

    // When and then
    this.mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.flag").value(true))
      .andExpect(jsonPath("$.code").value(StatusCode.OK))
      .andExpect(jsonPath("$.message").value("Retrieved all users successfully"))
      .andExpect(jsonPath("$.data.content[0].userId").value(u1.userId()))
      .andExpect(jsonPath("$.data.content[0].username").value(u1.username()))
      .andExpect(jsonPath("$.data.content[0].email").value(u1.email()))
      .andExpect(jsonPath("$.data.content[0].gender").value(u1.gender().toString()))
      .andExpect(jsonPath("$.data.content[1].userId").value(u2.userId()))
      .andExpect(jsonPath("$.data.content[1].username").value(u2.username()))
      .andExpect(jsonPath("$.data.content[1].email").value(u2.email()))
      .andExpect(jsonPath("$.data.content[1].gender").value(u2.gender().toString()))
      .andExpect(jsonPath("$.data.page.totalPages").value(1))
      .andExpect(jsonPath("$.data.page.totalElements").value(2))
      .andExpect(jsonPath("$.data.page.number").value(0))
      .andExpect(jsonPath("$.data.page.size").value(2));
  }

  @Test
  void testFindAllUsersInvalidSortField() throws Exception {
    // Given
    Integer offset = 0;
    Integer pageSize = 10;
    String sortBy = "invalid";
    String sortDir = "ASC";
    given(userService.readAll(eq(offset), eq(pageSize), eq(sortBy), eq(sortDir)))
      .willThrow(new InvalidSortFieldException("Invalid sort field"));

    // When and then
    this.mockMvc.perform(
        get("/api/users")
          .param("offset", offset.toString())
          .param("pageSize", pageSize.toString())
          .param("sortBy", sortBy)
          .param("sortDir", sortDir)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(jsonPath("$.flag").value(false))
      .andExpect(jsonPath("$.code").value(StatusCode.BAD_REQUEST))
      .andExpect(jsonPath("$.message").value("Invalid sort field"))
      .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testFindUserByIdSuccess() throws Exception {
    // Given
    UserResponseDto user = new UserResponseDto(
      1L,
      "Albus Dumbledore",
      "7sCm6@example.com",
      Gender.MALE
    );
    given(userService.readById(1L)).willReturn(user);

    // When and then
    this.mockMvc.perform(get("/api/users/1").accept(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.flag").value(true))
      .andExpect(jsonPath("$.code").value(StatusCode.FOUND))
      .andExpect(jsonPath("$.message").value("Retrieved user successfully"))
      .andExpect(jsonPath("$.data.userId").value(user.userId()))
      .andExpect(jsonPath("$.data.username").value(user.username()))
      .andExpect(jsonPath("$.data.email").value(user.email()));
  }

  @Test
  void testFindUserByIdNotFound() throws Exception {
    // Given
    given(userService.readById(1L)).willThrow(new ResourceNotFoundException("user", 1L));

    // When and then
    this.mockMvc.perform(get("/api/users/1").accept(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.flag").value(false))
      .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
      .andExpect(
        jsonPath("$.message").value("Resource {{ user }} not found with id {{ 1 }}")
      )
      .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testCreateUserSuccess() throws Exception {
    // Given
    UserCreationDto userCreationDto = new UserCreationDto(
      "John Doe",
      "7sCm6@example.com",
      "password",
      Gender.MALE
    );
    UserResponseDto userResponseDto = new UserResponseDto(
      1L,
      "John Doe",
      "7sCm6@example.com",
      Gender.MALE
    );
    given(userService.create(Mockito.any(UserCreationDto.class)))
      .willReturn(userResponseDto);

    // When and then
    this.mockMvc.perform(
        post("/api/users")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(userCreationDto))
      )
      .andExpect(jsonPath("$.flag").value(true))
      .andExpect(jsonPath("$.code").value(StatusCode.CREATED))
      .andExpect(jsonPath("$.message").value("User created successfully"))
      .andExpect(jsonPath("$.data.userId").value(userResponseDto.userId()))
      .andExpect(jsonPath("$.data.username").value(userResponseDto.username()))
      .andExpect(jsonPath("$.data.email").value(userResponseDto.email()));
  }

  @Test
  void testUpdateUserSuccess() throws Exception {
    // Given
    UserUpdateDto userUpdateDto = new UserUpdateDto(
      "John Doe",
      "7sCm6@example.com",
      Gender.MALE
    );
    UserResponseDto userResponseDto = new UserResponseDto(
      1L,
      "John Doe",
      "7sCm6@example.com",
      Gender.MALE
    );
    given(userService.update(1L, userUpdateDto)).willReturn(userResponseDto);

    // When and then
    this.mockMvc.perform(
        put("/api/users/1")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(userUpdateDto))
      )
      .andExpect(jsonPath("$.flag").value(true))
      .andExpect(jsonPath("$.code").value(StatusCode.OK))
      .andExpect(jsonPath("$.message").value("User updated successfully"))
      .andExpect(jsonPath("$.data.userId").value(userResponseDto.userId()))
      .andExpect(jsonPath("$.data.username").value(userResponseDto.username()))
      .andExpect(jsonPath("$.data.email").value(userResponseDto.email()));
  }

  @Test
  void testUpdateUserNotFound() throws Exception {
    // Given
    UserUpdateDto userUpdateDto = new UserUpdateDto(
      "John Doe",
      "7sCm6@example.com",
      Gender.MALE
    );
    given(userService.update(1L, userUpdateDto))
      .willThrow(new ResourceNotFoundException("user", 1L));

    // When and then
    this.mockMvc.perform(
        put("/api/users/1")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(userUpdateDto))
      )
      .andExpect(jsonPath("$.flag").value(false))
      .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
      .andExpect(
        jsonPath("$.message").value("Resource {{ user }} not found with id {{ 1 }}")
      )
      .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testDeleteUserSuccess() throws Exception {
    // Given
    Long userId = 1L;
    doNothing().when(userService).delete(userId);

    // When and then
    this.mockMvc.perform(delete("/api/users/1").accept(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.flag").value(true))
      .andExpect(jsonPath("$.code").value(StatusCode.NO_CONTENT))
      .andExpect(jsonPath("$.message").value("User deleted successfully"))
      .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testDeleteUserNotFound() throws Exception {
    // Given
    Long userId = 1L;
    doThrow(new ResourceNotFoundException("user", 1L)).when(userService).delete(userId);

    // When and then
    this.mockMvc.perform(delete("/api/users/1").accept(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.flag").value(false))
      .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
      .andExpect(
        jsonPath("$.message").value("Resource {{ user }} not found with id {{ 1 }}")
      )
      .andExpect(jsonPath("$.data").doesNotExist());
  }
}
