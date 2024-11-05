package com.kosmin.util;

import com.kosmin.model.Request;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ValidatorUtil {
  public static boolean missingCreditFields(
      List<Request.CreditRecordPayload> creditRecords, ConstraintValidatorContext context) {

    boolean errorsPresent = false;
    context.disableDefaultConstraintViolation();

    List<String> allErrors = new ArrayList<>();

    for (int i = 0; i < creditRecords.size(); i++) {
      Request.CreditRecordPayload currRecord = creditRecords.get(i);
      List<String> potentialErrors = invalidCreditRecord(currRecord);

      if (!potentialErrors.isEmpty()) {
        errorsPresent = true;
        final int index = i;

        potentialErrors.stream()
            .map(currString -> String.format("Index %d: %s", index, currString))
            .forEach(allErrors::add);
      }
    }

    for (String error : allErrors) {
      context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
    }

    return errorsPresent;
  }

  public static boolean validateTransactionDates(
      String checkingTransactionDate,
      List<Request.CreditRecordPayload> creditRecords,
      ConstraintValidatorContext constraintValidatorContext) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate checkingDate = LocalDate.parse(checkingTransactionDate, formatter);
      List<LocalDate> creditDates =
          creditRecords.stream()
              .map(Request.CreditRecordPayload::getCreditTransactionDate)
              .map(s -> LocalDate.parse(s, formatter))
              .toList();
      if (invalidCheckingDate(creditDates, checkingDate)) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext
            .buildConstraintViolationWithTemplate(
                "Checking transaction date must be within the month after all of the credit transaction dates")
            .addConstraintViolation();
        return false;
      }
      return true;
    } catch (DateTimeParseException e) {
      constraintValidatorContext.disableDefaultConstraintViolation();
      constraintValidatorContext
          .buildConstraintViolationWithTemplate(
              String.format("Exception Occurred parsing dates :: %s", e.getMessage()))
          .addConstraintViolation();
      return false;
    }
  }

  private static List<String> invalidCreditRecord(Request.CreditRecordPayload creditRecord) {
    List<String> errors = new ArrayList<>();
    if (StringUtils.isAllBlank(creditRecord.getCreditTransactionDate())) {
      errors.add("Credit Transaction Date cannot be blank");
    }
    if (StringUtils.isAllBlank(creditRecord.getCreditTransactionCategory())) {
      errors.add("Credit Transaction Category cannot be blank");
    }
    if (StringUtils.isAllBlank(creditRecord.getCreditTransactionType())) {
      errors.add("Credit Transaction Type cannot be blank");
    }
    if (creditRecord.getCreditTransactionAmount() == null) {
      errors.add("Credit Transaction Amount cannot be null");
    }
    if (StringUtils.isAllBlank(creditRecord.getCreditTransactionDescription())) {
      errors.add("Credit Transaction Description cannot be blank");
    }
    return errors;
  }

  private static boolean invalidCheckingDate(List<LocalDate> creditDates, LocalDate checkingDate) {
    for (LocalDate creditDate : creditDates) {
      LocalDate monthAfterCreditDateStart = creditDate.plusMonths(1).withDayOfMonth(1);
      LocalDate monthAfterCreditDateEnd =
          monthAfterCreditDateStart.withDayOfMonth(monthAfterCreditDateStart.lengthOfMonth());
      if (checkingDate.isBefore(monthAfterCreditDateStart)
          || checkingDate.isAfter(monthAfterCreditDateEnd)) {
        return true;
      }
    }
    return false;
  }
}
