package com.squelette.squelette_backend.audit;

import com.squelette.squelette_backend.exceptions.NotFoundException;
import com.squelette.squelette_backend.exceptions.RequestException;
import com.squelette.squelette_backend.exceptions.ServiceUnavailableException;
import com.squelette.squelette_backend.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditReadAspect {

    private final AuditService auditService;

    @Around("@annotation(fr.squelette.audit.AuditRead)")
    public Object auditEndpoint(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!isUrlAuditable(getUri())) {
            return joinPoint.proceed();
        }

        Object result;
        int status = 500;

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        HttpServletResponse httpServletResponse = ((ServletRequestAttributes) requestAttributes).getResponse();

        String httpMethod = httpServletRequest.getMethod();
        String url = httpServletRequest.getRequestURL().toString();
        String uri = httpServletRequest.getRequestURI();

        try {
            result = joinPoint.proceed();
            status = httpServletResponse.getStatus();
        } catch (Throwable e) {
            status = getStatus(e, status);
            throw e;
        } finally {
            try {
                auditService.logAuditEndpointAccess(url, status, httpMethod);
            } catch (Exception e) {
                log.error("Erreur lors de l'opération pour sauvegarder dans la table d'audit", e);
            }
        }

        return result;
    }

    private static boolean isUrlAuditable(final String uri) {
        return !uri.startsWith("swagger") && !uri.startsWith("/v3/api-docs") && !uri.startsWith("/actuator");
    }

    /**
     * Obtenir statut Exception
     *
     * @param e       Exception
     * @param status  Statut par défaut
     * @return statut exception
     */
    private static int getStatus(Throwable e, int status) {
        return switch (e) {
            case NotFoundException ex -> HttpStatus.NOT_FOUND.value();
            case RequestException ex -> HttpStatus.BAD_REQUEST.value();
            case UnauthorizedException ex -> HttpStatus.UNAUTHORIZED.value();
            case ServiceUnavailableException ex -> HttpStatus.SERVICE_UNAVAILABLE.value();
            default -> status;
        };
    }

    private String getUri() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return request.getRequestURI();
        }
        return "";
    }
}