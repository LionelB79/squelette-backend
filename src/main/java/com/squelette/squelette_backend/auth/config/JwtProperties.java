package com.squelette.squelette_backend.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "squelette.jwt")
public class JwtProperties {

    private String secret;
    private long expirationMs = 86400000; // 24 heures par défaut
    private String issuer = "gesta";
}
