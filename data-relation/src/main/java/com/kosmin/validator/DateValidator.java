package com.kosmin.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidatorImpl.class)
public @interface DateValidator {
  String message() default "Invalid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
