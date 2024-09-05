package org.uma.uma.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice  // Marks this class as a global exception handler for the entire application
public class GlobalExceptionHandler {

    // Handles exceptions of type EntityNotFoundException (commonly thrown when an entity cannot be found in the database)
    @ExceptionHandler(EntityNotFoundException.class)  // Specifies the type of exception to handle
    @ResponseStatus(HttpStatus.NOT_FOUND)  // Sets the HTTP status to 404 (Not Found) when this exception occurs
    public String handleEntityNotFound(EntityNotFoundException ex) {
        // Returns the "error" view (Thymeleaf template) when an entity is not found
        return "error";  // This maps to a Thymeleaf template named "error.html" that should display a user-friendly error message
    }

    // Handles exceptions of type IllegalArgumentException (commonly thrown when invalid arguments are passed)
    @ExceptionHandler(IllegalArgumentException.class)  // Specifies the type of exception to handle
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // Sets the HTTP status to 400 (Bad Request) when this exception occurs
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        // Returns the "error" view (Thymeleaf template) when an illegal argument is encountered
        return "error";  // This maps to a Thymeleaf template named "error.html" that should display a user-friendly error message
    }
}
