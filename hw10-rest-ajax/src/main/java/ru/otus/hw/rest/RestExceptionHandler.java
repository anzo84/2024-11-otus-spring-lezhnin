package ru.otus.hw.rest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.otus.hw.rest.model.ErrorDto;

import java.util.List;
import java.util.Locale;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorDto>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex, WebRequest request) {
        List<ErrorDto> errors = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(this::toErrorDto)
            .toList();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorDto>> handleConstraintViolationException(
        ConstraintViolationException ex, WebRequest request) {
        List<ErrorDto> errors = ex.getConstraintViolations().stream().map(this::toErrorDto).toList();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<List<ErrorDto>> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException ex, WebRequest request) {
        List<ErrorDto> errors = List.of (new ErrorDto().message(
            messageSource.getMessage("common.wrongRequestArgument", null, request.getLocale())));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private ErrorDto toErrorDto(FieldError fieldError) {
        return new ErrorDto().message(messageSource.getMessage(fieldError, Locale.getDefault()));
    }

    private ErrorDto toErrorDto(ConstraintViolation constraintViolation) {
        return new ErrorDto().message(constraintViolation.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
