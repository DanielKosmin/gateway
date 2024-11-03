package com.kosmin.validator;

import com.kosmin.model.ForeignKeyMappingPayload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class DateValidatorImpl
    implements ConstraintValidator<DateValidator, ForeignKeyMappingPayload> {

  @Override
  public boolean isValid(
      ForeignKeyMappingPayload foreignKeyMappingPayload,
      ConstraintValidatorContext constraintValidatorContext) {
    if (foreignKeyMappingPayload == null) return false;

    String checkingStartDate = foreignKeyMappingPayload.getCheckingStartDate();
    String checkingEndDate = foreignKeyMappingPayload.getCheckingEndDate();

    List<String> missingFields =
        Stream.of(
                StringUtils.isAllBlank(checkingStartDate) ? "checkingStartDate" : "",
                StringUtils.isAllBlank(checkingEndDate) ? "checkingEndDate" : "")
            .filter(field -> !field.isEmpty())
            .toList();

    if (missingFields.isEmpty()) {
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkingStart = LocalDate.parse(checkingStartDate, formatter);
        LocalDate checkingEnd = LocalDate.parse(checkingEndDate, formatter);

        if (checkingStart.equals(checkingEnd) || checkingStart.isAfter(checkingEnd)) {
          constraintValidatorContext.disableDefaultConstraintViolation();
          constraintValidatorContext
              .buildConstraintViolationWithTemplate(
                  String.format(
                      "checkingEndDate: %s must not be equal to and must be after checkingStartDate: %s",
                      checkingEnd, checkingStart))
              .addConstraintViolation();
          return false;
        }
        return true;
      } catch (DateTimeParseException e) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext
            .buildConstraintViolationWithTemplate(
                String.format(
                    "Exception Occurred for checkingStartDate: %s; checkingEndDate: %s :: %s",
                    checkingStartDate, checkingEndDate, e.getMessage()))
            .addConstraintViolation();
        return false;
      }
    }
    constraintValidatorContext.disableDefaultConstraintViolation();
    for (String field : missingFields) {
      constraintValidatorContext
          .buildConstraintViolationWithTemplate(field + " is required in 'yyyy-MM-dd' format")
          .addConstraintViolation();
    }
    return false;
  }
}
