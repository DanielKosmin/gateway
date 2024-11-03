package com.kosmin.aspect;

import static com.kosmin.util.ResponseEntityUtil.badRequestResponse;
import static com.kosmin.util.ResponseEntityUtil.internalServerErrorResponse;

import com.kosmin.exception.PrimaryKeyNotFoundException;
import com.kosmin.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DataRelationServiceHandler {

  @Around("execution(* com.kosmin.service.DataRelationService.*(..))")
  public Object handleServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      return joinPoint.proceed();
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<Response> handleException(Exception e) {
    log.error(e.getMessage());
    // trying to create tables when they already exist
    if (e instanceof BadSqlGrammarException
        && e.getCause().getMessage().toLowerCase().contains("already exists")) {
      return badRequestResponse(e.getCause().getMessage());
    }
    if (e instanceof RuntimeException
        && e.getMessage()
            .equalsIgnoreCase(
                "Driver org.postgresql.Driver claims to not accept jdbcUrl, ${POSTGRESQL_URL}")) {
      return internalServerErrorResponse(
          "DB Connection Strings not setup correctly for Table Creation");
    }
    if (e instanceof PrimaryKeyNotFoundException) {
      return badRequestResponse(e.getMessage());
    }
    return internalServerErrorResponse(e.getMessage());
  }
}
