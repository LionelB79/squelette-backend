package com.squelette.squelette_backend.auth.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "squelette.security")
public class SecurityProperties {

    private boolean enabled = true;
    private List<String> publicEndpoints = List.of("/auth/**");
    private List<String> allowedOrigins = List.of("http://localhost:5173");
}
