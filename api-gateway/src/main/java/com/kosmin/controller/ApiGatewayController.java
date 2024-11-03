package com.kosmin.controller;

import com.kosmin.model.Response;
import com.kosmin.service.ApiGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gateway/v1")
@RequiredArgsConstructor
public class ApiGatewayController {
  private final ApiGatewayService apiGatewayService;

  @PostMapping
  public ResponseEntity<Response> createTables() {
    return apiGatewayService.createTables();
  }
}
