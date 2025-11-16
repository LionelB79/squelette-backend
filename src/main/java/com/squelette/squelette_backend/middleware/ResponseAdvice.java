package com.squelette.squelette_backend.middleware;


import com.squelette.squelette_backend.model.Response;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final String SWAGGER = "swagger";
    private static final String API_DOCS_PATTERN = "/v3/api-docs";

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        String path = request.getURI().getPath();
        if (path.contains(SWAGGER) || path.startsWith(API_DOCS_PATTERN))
            return body;

        return mustNotBeConverted(body) ? body : new Response(null, null, body);
    }

    private boolean mustNotBeConverted(Object body) {
        return wasFileAsByteArray(body) || wasAlreadyCatchByResponseExceptionHandler(body);
    }

    private boolean wasFileAsByteArray(Object body) {
        return body instanceof byte[];
    }

    private boolean wasAlreadyCatchByResponseExceptionHandler(Object body) {
        return body instanceof Response && ((Response) body).getData() == null;
    }
}
