package com.devcambo.springinit.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.devcambo.springinit.exception.InvalidSortFieldException;
import com.devcambo.springinit.exception.ResourceNotFoundException;
import com.devcambo.springinit.mapper.UserMapperImpl;
import com.devcambo.springinit.model.dto.request.UserCreationDto;
import com.devcambo.springinit.model.dto.request.UserUpdateDto;
import com.devcambo.springinit.model.dto.response.UserResponseDto;
import com.devcambo.springinit.model.entity.User;
import com.devcambo.springinit.model.enums.Gender;
import com.devcambo.springinit.repo.UserRepo;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "dev")
class UserServiceImplTest {

  @Mock
  UserRepo userRepository;

  @Spy
  UserMapperImpl userMapperImpl;

  @InjectMocks
  UserServiceImpl userServiceImpl;

  List<User> users;

  User userExample;

  UserCreationDto userCreationDtoExample;

  UserUpdateDto userUpdateDtoExample;

  UserResponseDto userResponseDtoExample;

  Long userId;

  @BeforeEach
  void setUp() {
    userExample =
      new User(
        3L,
        "Example",
        "example@example.com",
        "password",
        Gender.MALE,
        "ADMIN,USER"
      );
    userId = userExample.getUserId();
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
    User u1 = new User(
      1L,
      "John Doe",
      "7sCm6@example.com",
      "password",
      Gender.MALE,
      "ADMIN,USER"
    );
    User u2 = new User(
      2L,
      "Kate Jones",
      "oUw0X@example.com",
      "password",
      Gender.FEMALE,
      "ADMIN,USER"
    );
    users = Arrays.asList(u1, u2);
  }

  @AfterEach
  void tearDown() {}

  @Test
  void testReadAllSuccess() {
    // Given
    Integer offset = 0;
    Integer pageSize = 10;
    String sortBy = "userId";
    String sortDir = "ASC";
    Page<User> usersPage = new PageImpl<>(users);
    given(userRepository.findAll(any(PageRequest.class))).willReturn(usersPage);

    // When
    Page<UserResponseDto> result = userServiceImpl.readAll(
      offset,
      pageSize,
      sortBy,
      sortDir
    );

    // Then
    assertNotNull(result);
    assertThat(result.getContent().size()).isEqualTo(users.size());

    // Verify
    verify(userRepository, times(1)).findAll(any(PageRequest.class));
  }

  @Test
  void testReadAllInvalidSortField() {
    // Given
    Integer offset = 0;
    Integer pageSize = 10;
    String sortBy = "invalidField";
    String sortDir = "ASC";

    // When
    Throwable exception = catchThrowable(() ->
      userServiceImpl.readAll(offset, pageSize, sortBy, sortDir)
    );

    // Then
    assertNotNull(exception);
    assertThat(exception)
      .isInstanceOf(InvalidSortFieldException.class)
      .hasMessage("Invalid sort field");
  }

  @Test
  void testReadByIdSuccess() {
    // Given
    given(userRepository.findById(userId)).willReturn(Optional.of(userExample));

    // When
    UserResponseDto result = userServiceImpl.readById(userId);

    // Then
    assertNotNull(result);
    assertThat(result.userId()).isEqualTo(userExample.getUserId());
    assertThat(result.username()).isEqualTo(userExample.getUsername());
    assertThat(result.email()).isEqualTo(userExample.getEmail());
    assertThat(result.gender()).isEqualTo(userExample.getGender());

    // Verify
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void testReadByIdNotFound() {
    // Given
    given(userRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

    // When
    Throwable exception = catchThrowable(() -> userServiceImpl.readById(userId));

    // Then
    assertNotNull(exception);
    assertThat(exception)
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessage(
        String.format("Resource {{ user }} not found with id {{ %d }}", userId)
      );

    // Verify
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void testCreateSuccess() {
    // Given
    given(userMapperImpl.toEntity(userCreationDtoExample)).willReturn(userExample);
    given(userRepository.save(userExample)).willReturn(userExample);
    given(userMapperImpl.toDto(userExample)).willReturn(userResponseDtoExample);

    // When
    UserResponseDto result = userServiceImpl.create(userCreationDtoExample);

    // Then
    assertNotNull(result);
    assertEquals(userResponseDtoExample, result);

    // Verify
    verify(userMapperImpl, times(1)).toEntity(userCreationDtoExample);
    verify(userRepository, times(1)).save(userExample);
    verify(userMapperImpl, times(1)).toDto(userExample);
  }

  @Test
  public void testUpdateSuccess() {
    // Given
    given(userRepository.findById(userId)).willReturn(Optional.of(userExample));
    given(userRepository.save(userExample)).willReturn(userExample);

    // When
    UserResponseDto updatedUser = userServiceImpl.update(userId, userUpdateDtoExample);

    // Then
    assertNotNull(updatedUser);
    assertEquals(userId, updatedUser.userId());
    assertEquals(userUpdateDtoExample.username(), updatedUser.username());
    assertEquals(userUpdateDtoExample.email(), updatedUser.email());
    assertEquals(userUpdateDtoExample.gender(), updatedUser.gender());

    // Verify
    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).save(userExample);
  }

  @Test
  public void testUpdateNotFound() {
    // Given
    given(userRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

    // When
    Throwable throwable = catchThrowable(() ->
      userServiceImpl.update(userId, Mockito.any(UserUpdateDto.class))
    );

    // Then
    assertThat(throwable)
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessage(
        String.format("Resource {{ user }} not found with id {{ %d }}", userId)
      );

    // Verify
    verify(userRepository, times(1)).findById(3L);
  }

  @Test
  void testDeleteSuccess() {
    // Given
    given(userRepository.findById(userId)).willReturn(Optional.of(userExample));
    doNothing().when(userRepository).deleteById(userId);

    // When
    userServiceImpl.delete(userId);

    // Then
    verify(userRepository, times(1)).deleteById(userId);
  }

  @Test
  void testDeleteNotFound() {
    // Given
    given(userRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

    // When
    Throwable exception = catchThrowable(() -> userServiceImpl.delete(userId));

    // Then
    assertNotNull(exception);
    assertThat(exception)
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessage(
        String.format("Resource {{ user }} not found with id {{ %d }}", userId)
      );

    // Verify
    verify(userRepository, times(1)).findById(userId);
  }
}
