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

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  UserRepo userRepository;

  @Spy
  UserMapperImpl userMapperImpl;

  @InjectMocks
  UserServiceImpl userServiceImpl;

  List<User> users;

  User example;

  @BeforeEach
  void setUp() {
    example = new User(3L, "Example", "example@example.com", "password", Gender.MALE);
    User u1 = new User(1L, "John Doe", "7sCm6@example.com", "password", Gender.MALE);
    User u2 = new User(2L, "Kate Jones", "oUw0X@example.com", "password", Gender.FEMALE);
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
    Long userId = 3L;
    given(userRepository.findById(userId)).willReturn(Optional.of(example));

    // When
    UserResponseDto result = userServiceImpl.readById(userId);

    // Then
    assertNotNull(result);
    assertThat(result.userId()).isEqualTo(example.getUserId());
    assertThat(result.username()).isEqualTo(example.getUsername());
    assertThat(result.email()).isEqualTo(example.getEmail());
    assertThat(result.gender()).isEqualTo(example.getGender());

    // Verify
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void testReadByIdNotFound() {
    // Given
    given(userRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

    // When
    Throwable exception = catchThrowable(() -> userServiceImpl.readById(3L));

    // Then
    assertNotNull(exception);
    assertThat(exception)
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessage("Resource {{ user }} not found with id {{ 3 }}");

    // Verify
    verify(userRepository, times(1)).findById(3L);
  }

  @Test
  void testCreateSuccess() {
    // Given
    UserCreationDto userCreationDto = new UserCreationDto(
      "John Doe",
      "7sCm6@example.com",
      "password",
      Gender.MALE
    );
    User user = new User(1L, "John Doe", "7sCm6@example.com", "password", Gender.MALE);
    UserResponseDto userResponseDto = new UserResponseDto(
      1L,
      "John Doe",
      "7sCm6@example.com",
      Gender.MALE
    );

    given(userMapperImpl.toEntity(userCreationDto)).willReturn(user);
    given(userRepository.save(user)).willReturn(user);
    given(userMapperImpl.toDto(user)).willReturn(userResponseDto);

    // When
    UserResponseDto result = userServiceImpl.create(userCreationDto);

    // Then
    assertNotNull(result);
    assertEquals(userResponseDto, result);

    // Verify
    verify(userMapperImpl, times(1)).toEntity(userCreationDto);
    verify(userRepository, times(1)).save(user);
    verify(userMapperImpl, times(1)).toDto(user);
  }

  @Test
  public void testUpdateSuccess() {
    // Given
    Long userId = 1L;
    User existingUser = new User();
    existingUser.setUserId(userId);
    existingUser.setUsername("johnDoe");
    existingUser.setEmail("johndoe@example.com");
    existingUser.setPassword("password123");
    existingUser.setGender(Gender.MALE);

    UserUpdateDto userUpdateDto = new UserUpdateDto(
      "janeDoe",
      "janedoe@example.com",
      Gender.FEMALE
    );

    given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
    given(userRepository.save(existingUser)).willReturn(existingUser);

    // When
    UserResponseDto updatedUser = userServiceImpl.update(userId, userUpdateDto);

    // Then
    assertNotNull(updatedUser);
    assertEquals(userId, updatedUser.userId());
    assertEquals("janeDoe", updatedUser.username());
    assertEquals("janedoe@example.com", updatedUser.email());
    assertEquals(Gender.FEMALE, updatedUser.gender());

    // Verify
    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).save(existingUser);
  }

  @Test
  public void testUpdateNotFound() {
    // Given
    given(userRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

    // When
    Throwable throwable = catchThrowable(() ->
      userServiceImpl.update(3L, Mockito.any(UserUpdateDto.class))
    );

    // Then
    assertThat(throwable)
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessage("Resource {{ user }} not found with id {{ 3 }}");

    // Verify
    verify(userRepository, times(1)).findById(3L);
  }

  @Test
  void testDeleteSuccess() {
    // Given
    Long userId = 3L;
    given(userRepository.findById(userId)).willReturn(Optional.of(example));
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
    Throwable exception = catchThrowable(() -> userServiceImpl.delete(3L));

    // Then
    assertNotNull(exception);
    assertThat(exception)
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessage("Resource {{ user }} not found with id {{ 3 }}");

    // Verify
    verify(userRepository, times(1)).findById(3L);
  }
}
