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
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.exception.EntityNotFoundException;
import ru.otus.hw.rest.model.ErrorDto;

import java.util.List;
import java.util.Locale;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<List<ErrorDto>>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex, WebRequest request) {
        return Flux.fromIterable(ex.getBindingResult().getFieldErrors())
            .map(this::toErrorDto)
            .collectList()
            .map(errors -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<List<ErrorDto>>> handleConstraintViolationException(
        ConstraintViolationException ex, WebRequest request) {
        return Flux.fromIterable(ex.getConstraintViolations())
            .map(this::toErrorDto)
            .collectList()
            .map(errors -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Mono<ResponseEntity<List<ErrorDto>>> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = messageSource.getMessage("common.wrongRequestArgument", null, request.getLocale());
        return Mono.just(ResponseEntity.badRequest()
            .body(List.of(new ErrorDto().message(message))));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<List<ErrorDto>>> handleIllegalArgumentException(
        IllegalArgumentException ex, WebRequest request) {
        String message = messageSource.getMessage(ex.getMessage(), null, request.getLocale());
        return Mono.just(ResponseEntity.badRequest()
            .body(List.of(new ErrorDto().message(message))));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public Mono<ResponseEntity<List<ErrorDto>>> handleEntityNotFoundException(
        EntityNotFoundException ex, WebRequest request) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(List.of(new ErrorDto().message(ex.getMessage()))));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<List<ErrorDto>>> handleResponseStatusException(
        ResponseStatusException ex, WebRequest request) {
        return Mono.just(ResponseEntity.status(ex.getStatusCode())
            .body(List.of(new ErrorDto().message(ex.getReason()))));
    }

    private ErrorDto toErrorDto(FieldError fieldError) {
        return new ErrorDto().message(messageSource.getMessage(fieldError, Locale.getDefault()));
    }

    private ErrorDto toErrorDto(ConstraintViolation<?> constraintViolation) {
        return new ErrorDto().message(constraintViolation.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Void>> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unexpected error", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}