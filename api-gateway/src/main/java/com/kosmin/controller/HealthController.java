package com.kosmin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("actuator")
public class HealthController {

  @GetMapping("health")
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("OK");
  }
}