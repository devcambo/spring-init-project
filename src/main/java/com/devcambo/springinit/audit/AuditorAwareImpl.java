package com.devcambo.springinit.audit;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

  @SuppressWarnings("NullableProblems")
  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("admin");
  }
}
