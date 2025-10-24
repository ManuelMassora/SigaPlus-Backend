package com.sigaplus.sigaplus.Controller;

// ... Imports ...

import com.sigaplus.sigaplus.dto.LoginRequest;
import com.sigaplus.sigaplus.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authenticationService;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public void login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        // 1. Autentica e gera o JWT
        String jwt = authenticationService.authenticate(loginRequest.email(), loginRequest.password());

        // 2. CRIA O COOKIE HTTP-ONLY
        Cookie jwtCookie = new Cookie("AUTH-TOKEN", jwt);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(3600); // Exemplo: 1 hora de validade

        response.addCookie(jwtCookie);

        // Retorna sucesso (ou um DTO de usuário, se necessário)
    }
}