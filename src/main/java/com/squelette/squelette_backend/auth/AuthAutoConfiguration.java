package com.squelette.squelette_backend.auth;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import com.squelette.squelette_backend.auth.config.JwtProperties;
import com.squelette.squelette_backend.auth.config.SecurityProperties;

@AutoConfiguration
@ComponentScan(basePackages = "com.squelette.squelette_backend.auth")
@EnableConfigurationProperties({JwtProperties.class, SecurityProperties.class})
public class AuthAutoConfiguration {
    // Auto-configuration pour le module auth
    // Scanne automatiquement tous les composants du package auth
}
