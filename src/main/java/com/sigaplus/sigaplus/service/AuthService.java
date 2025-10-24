package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Supondo que você tem um DTO de Requisição LoginRequest
// Supondo que você tem um JwtTokenService injetado

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtService;
    private final UsuarioRepository usuarioRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenService jwtService, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    public String authenticate(String email, String senha) {
        // 1. Autentica o usuário (lança exceção se as credenciais forem inválidas)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
        );

        // 2. Busca o objeto Usuario completo (com roles, etc.) para gerar o token.
        // Já sabemos que ele existe, pois a autenticação foi bem-sucedida.
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado após autenticação."));

        // 3. Gera e retorna o JWT, passando o objeto Usuario completo.
        // O JwtTokenService usará o email, roles, etc., do objeto Usuario.
        return jwtService.generateToken(usuario);
    }
}