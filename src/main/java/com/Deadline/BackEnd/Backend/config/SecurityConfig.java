package com.Deadline.BackEnd.Backend.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt.token")
public class SecurityConfig {
    String secretKey;
}
