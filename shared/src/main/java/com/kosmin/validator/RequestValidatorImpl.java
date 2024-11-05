package com.kosmin.validator;

import static com.kosmin.util.ValidatorUtil.missingCreditFields;
import static com.kosmin.util.ValidatorUtil.validateTransactionDates;

import com.kosmin.model.Request;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RequestValidatorImpl implements ConstraintValidator<RequestValidator, Request> {

  @Override
  public boolean isValid(Request request, ConstraintValidatorContext constraintValidatorContext) {
    String checkingTransactionDate = request.getCheckingTransactionDate();
    List<Request.CreditRecordPayload> creditRecords = request.getCreditRecords();

    List<String> missingFields =
        Stream.of(
                StringUtils.isAllBlank(checkingTransactionDate) ? "checkingTransactionDate" : "",
                CollectionUtils.isEmpty(creditRecords) ? "creditRecords" : "")
            .filter(field -> !field.isEmpty())
            .toList();

    if (missingFields.isEmpty()) {
      if (missingCreditFields(creditRecords, constraintValidatorContext)) {
        return false;
      } else
        return validateTransactionDates(
            checkingTransactionDate, creditRecords, constraintValidatorContext);
    } else {
      constraintValidatorContext.disableDefaultConstraintViolation();
      for (String field : missingFields) {
        if ("creditRecords".equalsIgnoreCase(field)) {
          constraintValidatorContext
              .buildConstraintViolationWithTemplate(field + " must have at least one credit record")
              .addConstraintViolation();
        } else {
          constraintValidatorContext
              .buildConstraintViolationWithTemplate(field + " is required in 'yyyy-MM-dd' format")
              .addConstraintViolation();
        }
      }
      return false;
    }
  }
}
