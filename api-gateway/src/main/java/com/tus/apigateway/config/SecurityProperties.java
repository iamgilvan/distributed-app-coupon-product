package com.tus.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {
    private String user;
    private String pass;
    private String jwtSecret;

    // Getters e Setters
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }
    public String getJwtSecret() { return jwtSecret; }
    public void setJwtSecret(String jwtSecret) { this.jwtSecret = jwtSecret; }
}