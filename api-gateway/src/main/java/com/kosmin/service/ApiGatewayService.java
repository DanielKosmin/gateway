package com.kosmin.service;

import com.kosmin.model.Response;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ApiGatewayService {
  private final DataRelationService dataRelationService;

  public ResponseEntity<Response> createRecords(MultipartFile file) {
    return Optional.ofNullable(file).isPresent()
        ? dataRelationService.insertTableRecords(file)
        : dataRelationService.createTables();
  }

  public ResponseEntity<Response> deleteRecords(
      Boolean credit, Boolean checking, Boolean dropTables) {
    return dataRelationService.deleteTableRecords(credit, checking, dropTables);
  }
}
