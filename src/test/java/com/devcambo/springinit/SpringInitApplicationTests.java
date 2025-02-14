package com.devcambo.springinit;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "dev")
class SpringInitApplicationTests {

  @Test
  void contextLoads() {
    String userRoles = "USER,ADMIN"; // Do not use a space

    var ls = List
      .of(userRoles.split(","))
      .stream()
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());
    System.out.println(ls);
    System.out.println(ls.get(0));
    System.out.println(ls.get(1));

    System.out.println("-------------------------");
    String tk = ls
      .stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.joining(","));
    System.out.println(tk);
  }
}
