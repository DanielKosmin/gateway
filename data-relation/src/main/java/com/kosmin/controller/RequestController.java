package com.kosmin.controller;

import com.kosmin.model.ForeignKeyMappingPayload;
import com.kosmin.model.Response;
import com.kosmin.service.DataRelationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("table_queries/v1")
@RequiredArgsConstructor
@Validated
public class RequestController {

  private final DataRelationService dataRelationService;

  @PostMapping("create")
  public ResponseEntity<Response> createTables() {
    return dataRelationService.createTables();
  }

  @PostMapping("insert")
  public ResponseEntity<Response> insertTableRecords(@RequestParam("file") MultipartFile file) {
    return dataRelationService.insertTableRecords(file);
  }

  @DeleteMapping("clear_records")
  public ResponseEntity<Response> clearTableRecords(
      @RequestParam(required = false) Boolean credit,
      @RequestParam(required = false) Boolean checking,
      @RequestParam(required = false) Boolean dropTables) {
    return dataRelationService.deleteTableRecords(credit, checking, dropTables);
  }

  @PutMapping("update")
  public ResponseEntity<Response> updateTableRecords(
      @Valid @RequestBody ForeignKeyMappingPayload payload) {
    return dataRelationService.updateTableRecords(payload);
  }
}
