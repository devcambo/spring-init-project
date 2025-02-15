package com.devcambo.springinit.exception;

import com.devcambo.springinit.constant.StatusCode;
import com.devcambo.springinit.model.base.ErrorInfo;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.awspring.cloud.s3.S3Exception;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // ===================================== 400 Exceptions =====================================
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorInfo handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<ObjectError> errors = ex.getBindingResult().getAllErrors();
    Map<String, String> errorDetails = new HashMap<>(errors.size());
    errors.forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errorDetails.put(fieldName, errorMessage);
    });
    return new ErrorInfo(
      false,
      StatusCode.BAD_REQUEST,
      "Invalid request parameters or body",
      errorDetails
    );
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorInfo handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    String errorDetails = "";
    if (ex.getCause() instanceof InvalidFormatException ifx) {
      if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
        errorDetails =
          String.format(
            "Invalid enum value: {{ %s }} for the field: {{ %s }}. The value must be one of: {{ %s }}",
            ifx.getValue(),
            ifx.getPath().get(ifx.getPath().size() - 1).getFieldName(),
            Arrays.toString(ifx.getTargetType().getEnumConstants())
          );
      }
    }
    return new ErrorInfo(
      false,
      StatusCode.BAD_REQUEST,
      "Invalid request parameters or body",
      errorDetails
    );
  }

  @ExceptionHandler(InvalidSortFieldException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorInfo handleInvalidSortFieldException(InvalidSortFieldException ex) {
    return new ErrorInfo(false, StatusCode.BAD_REQUEST, ex.getMessage());
  }

  // ===================================== 401 Exceptions =====================================
  /*
   * 1. Username or password is incorrect (UsernameNotFoundException, BadCredentialsException)
   * 2. Missing token (InsufficientAuthenticationException)
   * 3. Expired token (JwtTokenException)
   * 4. Invalid signature (JwtTokenException)
   * */
  @ExceptionHandler(
    value = { UsernameNotFoundException.class, BadCredentialsException.class }
  )
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  ErrorInfo handleUnauthorizedException(Exception ex) {
    return new ErrorInfo(
      false,
      StatusCode.UNAUTHORIZED,
      "username or password is incorrect",
      ex.getMessage()
    );
  }

  @ExceptionHandler(InsufficientAuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  ErrorInfo handleInsufficientAuthenticationException(
    InsufficientAuthenticationException ex
  ) {
    return new ErrorInfo(
      false,
      StatusCode.UNAUTHORIZED,
      "Login is required for this API endpoint",
      ex.getMessage()
    );
  }

  // ===================================== 403 Exceptions =====================================
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  ErrorInfo handleAccessDeniedException(AccessDeniedException ex) {
    return new ErrorInfo(false, StatusCode.FORBIDDEN, "No permission", ex.getMessage());
  }

  // ===================================== 404 Exceptions =====================================
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorInfo handleResourceNotFoundException(ResourceNotFoundException ex) {
    return new ErrorInfo(false, StatusCode.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorInfo handleNoHandlerFoundException(NoHandlerFoundException ex) {
    return new ErrorInfo(
      false,
      StatusCode.NOT_FOUND,
      "This API endpoint is not found",
      ex.getMessage()
    );
  }

  // ===================================== 405 Exceptions =====================================
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  ErrorInfo handleHttpRequestMethodNotSupportedException(
    HttpRequestMethodNotSupportedException ex
  ) {
    return new ErrorInfo(
      false,
      StatusCode.METHOD_NOT_ALLOWED,
      "This http method is not allowed for this API endpoint",
      ex.getMessage()
    );
  }

  // ===================================== 409 Exceptions =====================================
  // ===================================== 500 Exceptions =====================================
  @ExceptionHandler(S3Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  ErrorInfo handleS3Exception(S3Exception ex) {
    return new ErrorInfo(
      false,
      StatusCode.INTERNAL_SERVER_ERROR,
      ex.getMessage(),
      ex.getCause().getMessage()
    );
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  ErrorInfo handleOtherException(Exception ex) {
    return new ErrorInfo(
      false,
      StatusCode.INTERNAL_SERVER_ERROR,
      "An internal server error has occurred",
      ex.getMessage()
    );
  }
  // ===================================== 502 Exceptions =====================================
  // ===================================== 503 Exceptions =====================================
  // ===================================== 504 Exceptions =====================================
}
