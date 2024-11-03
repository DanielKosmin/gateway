package com.kosmin.controller;

import com.kosmin.model.Response;
import com.kosmin.service.ApiGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("gateway/v1")
@RequiredArgsConstructor
public class ApiGatewayController {
  private final ApiGatewayService apiGatewayService;

  @PostMapping
  public ResponseEntity<Response> createRecords(
      @RequestParam(name = "file", required = false) MultipartFile file) {
    return apiGatewayService.createRecords(file);
  }

  @DeleteMapping
  public ResponseEntity<Response> deleteRecords(
      @RequestParam(required = false) Boolean credit,
      @RequestParam(required = false) Boolean checking,
      @RequestParam(required = false) Boolean dropTables) {
    return apiGatewayService.deleteRecords(credit, checking, dropTables);
  }
}
