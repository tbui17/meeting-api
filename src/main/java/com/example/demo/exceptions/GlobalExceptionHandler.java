package com.example.demo.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatusCode status,
            @NotNull WebRequest request
    ) {
        var fieldErrors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                        Collectors.toMap(
                                FieldError::getField,
                                s -> s.getDefaultMessage() == null ? "" : s.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Invalid field values.", fieldErrors));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NotNull HttpMessageNotReadableException ex, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        logger
                .atInfo()
                .setCause(ex)
                .setMessage("Malformed request body")
                .log();
        return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }

    private record ErrorResponse(String message, Map<?, ?> body) {
        public ErrorResponse(String Message) {
            this(Message, Map.of());
        }
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handle(NoSuchElementException ex, WebRequest ignoredRequest) {
        logger.atInfo()
              .setCause(ex)
              .setMessage("Entity not found")
              .log();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ErrorResponse("Resource not found"));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handle(ConstraintViolationException ex, WebRequest ignoredRequest) {
        var violations = ex.getConstraintViolations()
                  .stream()
                          .collect(Collectors.toMap(
                                  ConstraintViolation::getPropertyPath,
                                  ConstraintViolation::getMessage
                          ));
        logger.atInfo()
              .setCause(ex)
              .setMessage("Validation exception")
              .log();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ErrorResponse("Invalid input",violations));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handle(DataIntegrityViolationException ex, WebRequest ignoredRequest) {

        logger.atInfo()
              .setCause(ex)
              .setMessage("Data integrity violation")
              .log();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ErrorResponse("Data integrity violation occurred. This is commonly caused by adding duplicate data."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex, WebRequest ignoredRequest) {
        logger.atError()
              .setCause(ex)
              .setMessage("Unhandled exception")
              .log();
        return ResponseEntity.internalServerError()
                             .body(new ErrorResponse("Server error"));
    }
}
