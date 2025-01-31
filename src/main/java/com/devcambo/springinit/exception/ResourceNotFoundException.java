package com.devcambo.springinit.exception;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String rsName, String id) {
    super(String.format("Resource {{ %s }} not found with id {{ %s }}", rsName, id));
  }

  public ResourceNotFoundException(String rsName, Long id) {
    super(String.format("Resource {{ %s }} not found with id {{ %d }}", rsName, id));
  }
}
