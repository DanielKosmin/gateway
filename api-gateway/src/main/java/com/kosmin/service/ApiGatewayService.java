package com.kosmin.service;

import com.kosmin.model.Request;
import com.kosmin.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ApiGatewayService {
  private final DataRelationService dataRelationService;

  public ResponseEntity<Response> createRecords() {
    return dataRelationService.createTables();
  }

  public ResponseEntity<Response> insertRecords(Request request) {
    return dataRelationService.insertTableRecords(null, request);
  }

  public ResponseEntity<Response> bulkUploadRecords(MultipartFile file) {
    return dataRelationService.insertTableRecords(file, null);
  }

  public ResponseEntity<Response> deleteRecords(
      Boolean credit, Boolean checking, Boolean dropTables) {
    return dataRelationService.deleteTableRecords(credit, checking, dropTables);
  }
}
