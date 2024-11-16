package com.kosmin.controller;

import com.kosmin.auth.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final JwtUtil jwtUtil;
  private final AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public ResponseEntity<String> login(
      @RequestParam String username, @RequestParam String password) {
    try {
      // authenticate against users specified in security config
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(username, password));

      if (authentication.isAuthenticated()) {
        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(token);
      } else {
        return ResponseEntity.status(401).body("Invalid credentials");
      }
    } catch (Exception e) {
      return ResponseEntity.status(401).body("Invalid credentials");
    }
  }
}
