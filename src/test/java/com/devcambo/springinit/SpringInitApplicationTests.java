package com.devcambo.springinit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "dev")
class SpringInitApplicationTests {

  @Test
  void contextLoads() {}
}
