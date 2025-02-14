package com.devcambo.springinit.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class AuthUtil {

  public String getCurrentLoginUserEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
