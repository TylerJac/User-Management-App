package org.uma.uma.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented  // Marks the annotation as documentable in the generated Javadocs
@Constraint(validatedBy = CustomValidation.CustomValidator.class)  // Specifies the validator class that will be used for validation
@Target({ ElementType.FIELD })  // Specifies that this annotation can only be applied to fields
@Retention(RetentionPolicy.RUNTIME)  // Specifies that this annotation is available at runtime
public @interface CustomValidation {
    String message() default "Invalid value";  // Default error message for validation failure
    Class<?>[] groups() default {};  // Allows specifying validation groups (not used in this case)
    Class<? extends Payload>[] payload() default {};  // Optional attribute that allows providing additional information (not used here)
    Type valueType();  // A custom attribute that specifies whether the validation is for USERNAME or PASSWORD

    // Enum that defines the possible validation types
    enum Type {
        USERNAME,  // For validating usernames
        PASSWORD   // For validating passwords
    }

    // Inner static class that implements the validation logic
    class CustomValidator implements ConstraintValidator<CustomValidation, String> {
        private Type valueType;  // Stores the validation type (USERNAME or PASSWORD) from the annotation

        @Override
        public void initialize(CustomValidation constraintAnnotation) {
            // Initialize the validator with the specified validation type (USERNAME or PASSWORD)
            this.valueType = constraintAnnotation.valueType();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            // Return false if the value is null (non-null validation can be added if required)
            if (value == null) {
                return false;
            }

            // Switch case to handle validation based on the specified valueType (USERNAME or PASSWORD)
            return switch (valueType) {
                case USERNAME ->
                    // Username must be 5-20 characters long and can contain alphanumeric characters and underscores
                        value.matches("^[a-zA-Z0-9_]{5,20}$");

                case PASSWORD ->
                    // Password must be at least 8 characters long, contain at least one uppercase letter and one digit
                        value.length() >= 8 && value.matches(".*[A-Z].*") && value.matches(".*[0-9].*");

                default -> false;  // Default case, returns false if the validation type is not recognized
            };
        }
    }
}
