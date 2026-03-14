package com.tus.apigateway.controller;

import com.tus.apigateway.config.SecurityProperties;
import com.tus.apigateway.service.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class AuthController {

    private final JwtUtils jwtUtils;
    private final SecurityProperties props;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtils jwtUtils, SecurityProperties props, PasswordEncoder passwordEncoder) {
        this.jwtUtils = jwtUtils;
        this.props = props;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Map<String, String> request) {
        String username = request.get( "username");
        String password = request.get("password");

        if (props.getUser().equals(username) && passwordEncoder.matches(password, passwordEncoder.encode(props.getPass()))) {
            String token = jwtUtils.generateToken(username);
            return Mono.just(ResponseEntity.ok(Map.of("token", token)));
        }

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}