package com.devcambo.springinit.util;

import com.devcambo.springinit.constant.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class AuthUtil {

  public String getCurrentLoginUserEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  public void sendAuthErrorResponse(HttpServletResponse response, String error)
    throws IOException {
    response.setStatus(StatusCode.UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("flag", false);
    errorResponse.put("code", StatusCode.UNAUTHORIZED);
    errorResponse.put("message", "Token is invalid or email not found");
    errorResponse.put("error", error);

    ObjectMapper objectMapper = new ObjectMapper();
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
