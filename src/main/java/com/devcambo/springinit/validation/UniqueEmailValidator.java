package com.devcambo.springinit.validation;

import com.devcambo.springinit.repo.UserRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

  private final UserRepo userRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return !userRepository.existsByEmail(value);
  }
}
