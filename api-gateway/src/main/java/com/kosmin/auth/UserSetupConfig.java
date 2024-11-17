package com.kosmin.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
public class UserSetupConfig {

  @Bean
  public CommandLineRunner createTestUser(
      JdbcUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
    return args -> {
      if (!userDetailsManager.userExists("admin")) {
        userDetailsManager.createUser(
            User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build());
      }
    };
  }
}
