package com.devcambo.springinit.service;

import com.devcambo.springinit.model.dto.request.UserCreationDto;
import com.devcambo.springinit.model.dto.request.UserUpdateDto;
import com.devcambo.springinit.model.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;

public interface UserService {
  /**
   * Retrieves a paginated list of all users.
   *
   * @param offset The offset from which to start retrieving users.
   * @param pageSize The number of users to retrieve per page.
   * @param sortBy The field to sort the users by.
   * @param sortDir The direction to sort the users (ASC or DESC).
   * @return A paginated list of UserResponseDto objects.
   */
  Page<UserResponseDto> readAll(
    Integer offset,
    Integer pageSize,
    String sortBy,
    String sortDir
  );

  /**
   * Retrieves a user by their ID.
   *
   * @param userId The ID of the user to retrieve.
   * @return The UserResponseDto object for the specified user.
   */
  UserResponseDto readById(Long userId);

  /**
   * Creates a new user.
   *
   * @param userCreationDto The UserRequestDto object containing the user's details.
   * @return The UserResponseDto object for the newly created user.
   */
  UserResponseDto create(UserCreationDto userCreationDto);

  /**
   * Updates an existing user.
   *
   * @param userId The ID of the user to update.
   * @param userUpdateDto The UserRequestDto object containing the updated user's details.
   * @return The UserResponseDto object for the updated user.
   */
  UserResponseDto update(Long userId, UserUpdateDto userUpdateDto);

  /**
   * Deletes a user by their ID.
   *
   * @param userId The ID of the user to delete.
   */
  void delete(Long userId);
}
