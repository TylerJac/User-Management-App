package org.uma.uma.dto;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomValidation.CustomValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidation {
    String message() default "Invalid value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Type valueType();

    enum Type {
        USERNAME, PASSWORD
    }

    class CustomValidator implements ConstraintValidator<CustomValidation, String> {
        private Type valueType;

        @Override
        public void initialize(CustomValidation constraintAnnotation) {
            this.valueType = constraintAnnotation.valueType();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return false;
            }
            return switch (valueType) {
                case USERNAME -> value.matches("^[a-zA-Z0-9_]{5,20}$");
                case PASSWORD -> value.length() >= 8 && value.matches(".*[A-Z].*") && value.matches(".*[0-9].*");
                default -> false;
            };
        }
    }
}

