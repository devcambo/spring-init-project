package com.devcambo.springinit.config;

import com.devcambo.springinit.constant.SwaggerConstant;
import com.devcambo.springinit.exception.CustomAccessDeniedHandler;
import com.devcambo.springinit.exception.CustomAuthenticationEntryPoint;
import com.devcambo.springinit.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthFilter authFilter;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers(SwaggerConstant.SWAGGER_WHITE_LIST_URL)
          .permitAll()
          .requestMatchers("/api/auth/**")
          .permitAll()
          .requestMatchers("/api/buckets/**")
          .hasAuthority("SUPER_ADMIN")
          .anyRequest()
          .authenticated()
      )
      .csrf(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .logout(AbstractHttpConfigurer::disable)
      .rememberMe(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .sessionManagement(sess ->
        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
      .exceptionHandling(ex ->
        ex
          .authenticationEntryPoint(customAuthenticationEntryPoint)
          .accessDeniedHandler(customAccessDeniedHandler)
      )
      .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
    throws Exception {
    return config.getAuthenticationManager();
  }
}
