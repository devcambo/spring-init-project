package com.devcambo.springinit.exception;

import com.devcambo.springinit.constant.StatusCode;
import com.devcambo.springinit.model.base.ErrorResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
    return new ErrorResponse(false, StatusCode.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(
    {
      MethodArgumentNotValidException.class,
      HttpMessageNotReadableException.class,
      InvalidSortFieldException.class,
    }
  )
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse handleBadRequestException(Exception exception) {
    if (exception instanceof MethodArgumentNotValidException invalidArgumentException) {
      List<ObjectError> errors = invalidArgumentException
        .getBindingResult()
        .getAllErrors();
      Map<String, String> errorMap = new HashMap<>(errors.size());
      errors.forEach(error -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errorMap.put(fieldName, errorMessage);
      });
      return new ErrorResponse(
        false,
        StatusCode.BAD_REQUEST,
        "Invalid request parameters",
        errorMap
      );
    } else if (exception instanceof HttpMessageNotReadableException readException) {
      String errorDetails = getErrorDetailsOfHttpMessageNotReadableException(
        readException
      );
      return new ErrorResponse(
        false,
        StatusCode.BAD_REQUEST,
        "Invalid request parameters",
        errorDetails
      );
    } else {
      return new ErrorResponse(false, StatusCode.BAD_REQUEST, exception.getMessage());
    }
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  ErrorResponse handleOtherException(Exception ex) {
    return new ErrorResponse(
      false,
      StatusCode.INTERNAL_SERVER_ERROR,
      "Something went wrong",
      ex.getMessage()
    );
  }

  private String getErrorDetailsOfHttpMessageNotReadableException(
    HttpMessageNotReadableException exception
  ) {
    if (exception.getCause() instanceof InvalidFormatException formatException) {
      if (
        formatException.getTargetType() != null &&
        formatException.getTargetType().isEnum()
      ) {
        return String.format(
          "Invalid enum value {{ %s }} for the field {{ %s }}. The value must be one of this enum values: {{ %s }} ",
          formatException.getValue(),
          formatException
            .getPath()
            .get(formatException.getPath().size() - 1)
            .getFieldName(),
          Arrays.toString(formatException.getTargetType().getEnumConstants())
        );
      }
    }
    return "";
  }
}
