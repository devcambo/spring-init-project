package com.devcambo.springinit.filter;

import com.devcambo.springinit.constant.JWTConstant;
import com.devcambo.springinit.exception.JwtTokenException;
import com.devcambo.springinit.service.JwtService;
import com.devcambo.springinit.service.UserInfoService;
import com.devcambo.springinit.util.AuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserInfoService userDetailsService;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    // TODO: implement logic to filter out for all endpoints that don't require authentication
    return (
      request.getServletPath().equals("/api/auth/login") ||
      request.getServletPath().equals("/api/auth/register")
    );
  }

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      String authHeader = request.getHeader(JWTConstant.JWT_TOKEN_HEADER);
      String token = null;
      String subject = null;

      if (authHeader != null && authHeader.startsWith(JWTConstant.JWT_TOKEN_PREFIX)) {
        token = authHeader.substring(JWTConstant.JWT_TOKEN_PREFIX.length());
        subject = jwtService.extractSubject(token);
      }

      if (
        subject != null && SecurityContextHolder.getContext().getAuthentication() == null
      ) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);

        if (jwtService.validateToken(token, userDetails)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
          );
          authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
          );
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
      filterChain.doFilter(request, response);
    } catch (JwtTokenException | UsernameNotFoundException e) {
      AuthUtil.sendAuthErrorResponse(response, e.getMessage());
    }
  }
}
