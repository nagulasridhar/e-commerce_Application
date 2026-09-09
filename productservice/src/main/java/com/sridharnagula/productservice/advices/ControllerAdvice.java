package com.sridharnagula.productservice.advices;

import com.sridharnagula.productservice.dtos.ErrorDTO;
import com.sridharnagula.productservice.exceptions.CategoryNotFoundException;
import com.sridharnagula.productservice.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductNotFoundException(ProductNotFoundException exception){
        return error(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleCategoryNotFoundException(CategoryNotFoundException exception){
        return error(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    // fakeStoreCategoryService's create/update/delete/getById methods throw this for
    // operations the third-party API doesn't support (see that class) - mapped to 405
    // rather than a generic 500 so it's clear the *operation*, not the server, is the issue.
    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorDTO> handleUnsupportedOperationException(UnsupportedOperationException exception){
        return error(exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        return error(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleUnexpectedException(Exception exception) {
        return error("Unexpected error: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDTO> error(String message, HttpStatus status) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(message);
        return new ResponseEntity<>(errorDTO, status);
    }
}
