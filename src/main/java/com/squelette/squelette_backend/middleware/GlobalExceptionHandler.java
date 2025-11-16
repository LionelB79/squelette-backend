package com.squelette.squelette_backend.middleware;


import com.squelette.squelette_backend.exceptions.*;
import com.squelette.squelette_backend.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GlobalExceptionHandler {

    private static final String DEFAULT_PREFIX = "ERROR";
    private static final CodeMessage EXCEPTION_UNKNOWN = new CodeMessage(1, "EXCEPTION_UNKNOWN");
    private static final CodeMessage MAX_FILE_SIZE_EXCEEDED = new CodeMessage(2, "MAX_FILE_SIZE_EXCEEDED");
    private static final CodeMessage ROUTE_UNKNOWN = new CodeMessage(3, "ROUTE_UNKNOWN");
    private static final CodeMessage JAKARTA_VALIDATION = new CodeMessage(4, "JAKARTA_VALIDATION: %s");

    static final Map<Class<? extends CustomException>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of(
            NotFoundException.class, HttpStatus.NOT_FOUND,
            RequestException.class, HttpStatus.BAD_REQUEST,
            ForbiddenException.class, HttpStatus.FORBIDDEN,
            AuthenticationException.class, HttpStatus.UNAUTHORIZED,
            UnauthorizedException.class, HttpStatus.UNAUTHORIZED,
            ServiceUnavailableException.class, HttpStatus.SERVICE_UNAVAILABLE
    );

    /**
     * Méthode centralisée pour gérer et formater toutes les exceptions
     */
    public ResponseEntity<Object> handle(Exception exception) {
        // Réponse par défaut
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        CodeMessage codeMessage = EXCEPTION_UNKNOWN;
        String prefix = DEFAULT_PREFIX;
        boolean unknownException = false;

        // Gestion des différents types d'exceptions
        if (exception instanceof CustomException e) {
            codeMessage = e.getCodeMessage();
            status = EXCEPTION_STATUS_MAP.getOrDefault(e.getClass(), status);
        } else if (exception instanceof MethodArgumentNotValidException e) {
            codeMessage = JAKARTA_VALIDATION.formatMessage(
                    e.getBindingResult().getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.joining(", "))
            );
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof MaxUploadSizeExceededException) {
            codeMessage = MAX_FILE_SIZE_EXCEEDED;
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof NoResourceFoundException) {
            codeMessage = ROUTE_UNKNOWN;
            status = HttpStatus.NOT_FOUND;
        } else {
            unknownException = true;
        }

        if (unknownException) {
            log.error(exception.getMessage(), exception);
        } else {
            log.warn(exception.getMessage(), exception);
        }

        return ResponseEntity
                .status(status)
                .body(
                        new Response(
                                String.format("%s_%05d", prefix.toUpperCase(), codeMessage.getCode()),
                                codeMessage.getMessage(),
                                null
                        )
                );
    }
}