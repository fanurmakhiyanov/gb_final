package ru.fanur.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.fanur.dto.response.Error;
import ru.fanur.exception.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionControllerAdvice implements AuthenticationEntryPoint {
    private final AtomicInteger counterErrors = new AtomicInteger();
    private static final String DEFAULT_PREFIX_MESSAGE_ERROR_VALIDATION = "Error input data: ";
    private final ObjectMapper mapper;

    // redirect 403 -> 401
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().print(mapper.writeValueAsString(buildError("Unauthorized error")));
    }

    private Error buildError(String message) {
        log.error("exception handling: {}", message);
        return new Error(message, counterErrors.getAndIncrement());
    }

    @ExceptionHandler(value = {UploadFileException.class, AuthenticationException.class,
            IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error exceptionUploadFileHandler(Exception e) {
        return buildError(e.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error exceptionObjectValidationHandler(MethodArgumentNotValidException e) {
        var message = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining());
        return buildError(DEFAULT_PREFIX_MESSAGE_ERROR_VALIDATION + message);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error exceptionArgumentValidationHandler(ConstraintViolationException e) {
        return buildError(DEFAULT_PREFIX_MESSAGE_ERROR_VALIDATION + e.getMessage());
    }

    @ExceptionHandler(value = {FileNotFoundException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error exceptionFindFileHandler(Exception e) {
        return buildError(e.getMessage());
    }
}