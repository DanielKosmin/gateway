package com.kosmin.service;

import com.kosmin.model.Response;
import com.kosmin.model.Status;
// import com.kosmin.service.database.operations.DbOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiGatewayService {
  //  private final DbOperationsService dbOperationsService;

  public ResponseEntity<Response> createTables() {
    //    dbOperationsService.createTables();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Response.builder().status(Status.SUCCESS.getValue()).build());
  }
}
