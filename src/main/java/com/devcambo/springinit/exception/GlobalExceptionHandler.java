package com.devcambo.springinit.exception;

import com.devcambo.springinit.constant.StatusCode;
import com.devcambo.springinit.model.base.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
    return new ErrorResponse(false, StatusCode.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(InvalidSortFieldException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse handleInvalidSortFieldException(InvalidSortFieldException ex) {
    return new ErrorResponse(false, StatusCode.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
    List<ObjectError> errors = ex.getBindingResult().getAllErrors();
    Map<String, String> map = new HashMap<>(errors.size());
    errors.forEach((error) -> {
      String key = ((FieldError) error).getField();
      String val = error.getDefaultMessage();
      map.put(key, val);
    });
    return new ErrorResponse(false, StatusCode.BAD_REQUEST, "Invalid request parameters", map);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  ErrorResponse handleOtherException(Exception ex) {
    return new ErrorResponse(false, StatusCode.INTERNAL_SERVER_ERROR, "Something went wrong", ex.getMessage());
  }

}
