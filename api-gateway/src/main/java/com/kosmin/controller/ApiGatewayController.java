package com.kosmin.controller;

import com.kosmin.model.Request;
import com.kosmin.model.Response;
import com.kosmin.service.ApiGatewayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("gateway/v1")
@RequiredArgsConstructor
@Validated
public class ApiGatewayController {
  private final ApiGatewayService apiGatewayService;

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<Response> postFile(
      @RequestParam(value = "file", required = false) MultipartFile file) {
    return apiGatewayService.bulkUploadRecords(file);
  }

  @PostMapping(consumes = "application/json")
  public ResponseEntity<Response> createRecords(@Valid @RequestBody Request request) {
    return apiGatewayService.insertRecords(request);
  }

  @DeleteMapping
  public ResponseEntity<Response> deleteRecords(
      @RequestParam(required = false) Boolean credit,
      @RequestParam(required = false) Boolean checking,
      @RequestParam(required = false) Boolean dropTables) {
    return apiGatewayService.deleteRecords(credit, checking, dropTables);
  }
}
