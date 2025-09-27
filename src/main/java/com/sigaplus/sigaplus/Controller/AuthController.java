package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.LoginRequest;
import com.sigaplus.sigaplus.dto.LoginResponse;
import com.sigaplus.sigaplus.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> auth(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenicateCliente(loginRequest));
    }
}