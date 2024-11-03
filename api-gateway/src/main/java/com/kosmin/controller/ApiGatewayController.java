package com.kosmin.controller;

import com.kosmin.service.ApiGatwayService;
import com.kosmin.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gateway/v1")
@RequiredArgsConstructor
public class ApiGatewayController {
  private final ApiGatwayService apiGatwayService;


  @PostMapping
  public ResponseEntity<Response> createTables() {
    return apiGatwayService.createTables();
  }
}
