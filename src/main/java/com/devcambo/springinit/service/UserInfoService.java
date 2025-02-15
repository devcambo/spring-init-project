package com.devcambo.springinit.service;

import com.devcambo.springinit.model.base.UserInfoDetails;
import com.devcambo.springinit.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {

  @Autowired
  private UserRepo repository;

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    return repository
      .findByEmail(username)
      .map(UserInfoDetails::new)
      .orElseThrow(() ->
        new UsernameNotFoundException(
          String.format("User not found with email {{ %s }}", username)
        )
      );
  }
}
