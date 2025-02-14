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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(value = "dev")
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;

  UserCreationDto userCreationDtoExample;

  UserUpdateDto userUpdateDtoExample;

  UserResponseDto userResponseDtoExample;

  UserResponseDto userResponseDtoExample2;

  Long userId;

  @BeforeEach
  void setUp() {
    userCreationDtoExample =
      new UserCreationDto(
        "Create Example",
        "create@example.com",
        "createPassword",
        Gender.MALE
      );
    userUpdateDtoExample =
      new UserUpdateDto("Update Example", "update@example.com", Gender.MALE);
    userResponseDtoExample =
      new UserResponseDto(3L, "Response Example", "response@example.com", Gender.MALE);
    userResponseDtoExample2 =
      new UserResponseDto(4L, "Response2 Example", "response2@example.com", Gender.MALE);
    userId = userResponseDtoExample.userId();
  }

  @AfterEach
  void tearDown() {}

  @Test
  void testFindAllUsersSuccess() throws Exception {
    // Given
    List<UserResponseDto> users = Arrays.asList(
      userResponseDtoExample,
      userResponseDtoExample2
    );
    Page<UserResponseDto> usersPage = new PageImpl<>(users);
    given(userService.readAll(0, 10, "userId", "ASC")).willReturn(usersPage);

    // When and then
    this.mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.flag").value(true))
      .andExpect(jsonPath("$.code").value(StatusCode.OK))
      .andExpect(jsonPath("$.message").value("Retrieved all users successfully"))
      .andExpect(
        jsonPath("$.data.content[0].userId").value(userResponseDtoExample.userId())
      )
      .andExpect(
        jsonPath("$.data.content[0].username").value(userResponseDtoExample.username())
      )
      .andExpect(
        jsonPath("$.data.content[0].email").value(userResponseDtoExample.email())
      )
      .andExpect(
        jsonPath("$.data.content[0].gender")
          .value(userResponseDtoExample.gender().toString())
      )
      .andExpect(
        jsonPath("$.data.content[1].userId").value(userResponseDtoExample2.userId())
      )
      .andExpect(
        jsonPath("$.data.content[1].username").value(userResponseDtoExample2.username())
      )
      .andExpect(
        jsonPath("$.data.content[1].email").value(userResponseDtoExample2.email())
      )
      .andExpect(
        jsonPath("$.data.content[1].gender")
          .value(userResponseDtoExample2.gender().toString())
      )
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
    given(userService.readById(userId)).willReturn(userResponseDtoExample);

    // When and then
    this.mockMvc.perform(get("/api/users/" + userId).accept(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.flag").value(true))
      .andExpect(jsonPath("$.code").value(StatusCode.FOUND))
      .andExpect(jsonPath("$.message").value("Retrieved user successfully"))
      .andExpect(jsonPath("$.data.userId").value(userResponseDtoExample.userId()))
      .andExpect(jsonPath("$.data.username").value(userResponseDtoExample.username()))
      .andExpect(jsonPath("$.data.email").value(userResponseDtoExample.email()));
  }

  @Test
  void testFindUserByIdNotFound() throws Exception {
    // Given
    given(userService.readById(Mockito.any(Long.class)))
      .willThrow(new ResourceNotFoundException("user", userId));

    // When and then
    this.mockMvc.perform(get("/api/users/" + userId).accept(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.flag").value(false))
      .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
      .andExpect(
        jsonPath("$.message")
          .value(String.format("Resource {{ user }} not found with id {{ %d }}", userId))
      )
      .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testCreateUserSuccess() throws Exception {
    // Given
    given(userService.create(Mockito.any(UserCreationDto.class)))
      .willReturn(userResponseDtoExample);

    // When and then
    this.mockMvc.perform(
        post("/api/users")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(userCreationDtoExample))
      )
      .andExpect(jsonPath("$.flag").value(true))
      .andExpect(jsonPath("$.code").value(StatusCode.CREATED))
      .andExpect(jsonPath("$.message").value("User created successfully"))
      .andExpect(jsonPath("$.data.userId").value(userResponseDtoExample.userId()))
      .andExpect(jsonPath("$.data.username").value(userResponseDtoExample.username()))
      .andExpect(jsonPath("$.data.email").value(userResponseDtoExample.email()));
  }

  @Test
  void testUpdateUserSuccess() throws Exception {
    // Given
    given(userService.update(userId, userUpdateDtoExample))
      .willReturn(userResponseDtoExample);

    // When and then
    this.mockMvc.perform(
        put("/api/users/" + userId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(userUpdateDtoExample))
      )
      .andExpect(jsonPath("$.flag").value(true))
      .andExpect(jsonPath("$.code").value(StatusCode.OK))
      .andExpect(jsonPath("$.message").value("User updated successfully"))
      .andExpect(jsonPath("$.data.userId").value(userResponseDtoExample.userId()))
      .andExpect(jsonPath("$.data.username").value(userResponseDtoExample.username()))
      .andExpect(jsonPath("$.data.email").value(userResponseDtoExample.email()));
  }

  @Test
  void testUpdateUserNotFound() throws Exception {
    // Given
    given(userService.update(userId, userUpdateDtoExample))
      .willThrow(new ResourceNotFoundException("user", userId));

    // When and then
    this.mockMvc.perform(
        put("/api/users/" + userId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(userUpdateDtoExample))
      )
      .andExpect(jsonPath("$.flag").value(false))
      .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
      .andExpect(
        jsonPath("$.message")
          .value(String.format("Resource {{ user }} not found with id {{ %d }}", userId))
      )
      .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testDeleteUserSuccess() throws Exception {
    // Given
    doNothing().when(userService).delete(userId);

    // When and then
    this.mockMvc.perform(
        delete("/api/users/" + userId).accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(jsonPath("$.flag").value(true))
      .andExpect(jsonPath("$.code").value(StatusCode.NO_CONTENT))
      .andExpect(jsonPath("$.message").value("User deleted successfully"))
      .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testDeleteUserNotFound() throws Exception {
    // Given
    doThrow(new ResourceNotFoundException("user", userId))
      .when(userService)
      .delete(userId);

    // When and then
    this.mockMvc.perform(
        delete("/api/users/" + userId).accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(jsonPath("$.flag").value(false))
      .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
      .andExpect(
        jsonPath("$.message")
          .value(String.format("Resource {{ user }} not found with id {{ %d }}", userId))
      )
      .andExpect(jsonPath("$.data").doesNotExist());
  }
}
