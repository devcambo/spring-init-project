package com.devcambo.springinit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringInitApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringInitApplication.class, args);
  }
}
