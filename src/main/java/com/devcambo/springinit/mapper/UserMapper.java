package com.devcambo.springinit.mapper;

import com.devcambo.springinit.model.dto.request.UserRequestDto;
import com.devcambo.springinit.model.dto.response.UserResponseDto;
import com.devcambo.springinit.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mappings(
    {
      @Mapping(source = "userId", target = "userId"),
      @Mapping(source = "email", target = "email"),
      @Mapping(source = "gender", target = "gender"),
    }
  )
  UserResponseDto toDto(User user);

  @Mappings(
    {
      @Mapping(source = "email", target = "email"),
      @Mapping(source = "gender", target = "gender"),
    }
  )
  User toEntity(UserRequestDto userRequestDto);

  @Mapping(target = "userId", ignore = true)
  void updateFromDto(UserRequestDto dto, @MappingTarget User entity);
}
