package com.devcambo.springinit.filter;

import com.devcambo.springinit.constant.JWTConstant;
import com.devcambo.springinit.service.JwtService;
import com.devcambo.springinit.service.UserInfoService;
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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserInfoService userDetailsService;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    String authHeader = request.getHeader(JWTConstant.JWT_TOKEN_HEADER);
    String token = null;
    String subject = null;

    if (authHeader != null && authHeader.startsWith(JWTConstant.JWT_TOKEN_PREFIX)) {
      token = authHeader.substring(7);
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
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
