package com.devcambo.springinit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueBucketValidator.class)
public @interface UniqueBucket {
  String message() default "Bucket already exists!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
