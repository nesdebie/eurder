package org.example.eurder.api;

import org.example.eurder.service.exception.*;
import jakarta.servlet.http.HttpServletResponse;
import org.example.eurder.service.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.UUID;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    protected void notFound(NotFoundException ex, HttpServletResponse response) throws IOException {
        logger.warn("Not found: {}", ex.getMessage());
        response.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected void unauthorized(UnauthorizedException ex, HttpServletResponse response) throws IOException {
        logger.warn("Unauthorized: {}", ex.getMessage());
        response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    protected void badRequest(BadRequestException ex, HttpServletResponse response) throws IOException {
        logger.warn("Bad Request: {}", ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected void unexpectedError(Exception ex, HttpServletResponse response) throws IOException {
        String errorId = UUID.randomUUID().toString();
        logger.error("Unexpected error: {}", errorId, ex);
        response.sendError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong. Please contact support with code: %s".formatted(errorId)
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    protected void forbidden(ForbiddenException ex, HttpServletResponse response) throws IOException {
        logger.warn("Forbidden: {}", ex.getMessage());
        response.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
}
