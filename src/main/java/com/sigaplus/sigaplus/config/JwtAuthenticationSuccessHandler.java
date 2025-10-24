package com.sigaplus.sigaplus.config;

import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.UsuarioRepository;
import com.sigaplus.sigaplus.service.JwtTokenService;
import com.sigaplus.sigaplus.repo.PerfilEstudanteRepository;
import com.sigaplus.sigaplus.service.CustomOidcUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenService jwtService;
    private final PerfilEstudanteRepository perfilEstudanteRepository;
    private final UsuarioRepository usuarioRepository; // Injetar o repositório

    public JwtAuthenticationSuccessHandler(JwtTokenService jwtService,
                                           PerfilEstudanteRepository perfilEstudanteRepository,
                                           UsuarioRepository usuarioRepository) { // Adicionar injeção
        this.jwtService = jwtService;
        this.perfilEstudanteRepository = perfilEstudanteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        // O Principal está disponível AQUI!
        CustomOidcUser principal = (CustomOidcUser) authentication.getPrincipal();
        String userEmail = principal.getEmail();

        // 1. BUSCA O USUÁRIO COMPLETO
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado após JIT."));

        // 2. GERAÇÃO DO TOKEN (passando o objeto Usuario)
        String jwt = jwtService.generateToken(usuario);

        // 3. CRIA O COOKIE HTTP-ONLY
        Cookie jwtCookie = new Cookie("AUTH-TOKEN", jwt);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(3600); // 1 hora
        //.setSecure(true) para produção (HTTPS)
        response.addCookie(jwtCookie);

        // 4. VERIFICAÇÃO DO PERFIL E REDIRECIONAMENTO
        // Usa o método correto do repositório (findByUsuarioId ou existsByUsuarioId)
        boolean perfilCompleto = perfilEstudanteRepository.findByUsuarioId(usuario.getId()).isPresent();

        String redirectUrl;
        if (perfilCompleto) {
            // USUÁRIO REGISTRADO COMPLETAMENTE
            redirectUrl = "http://localhost:5173/dashboard";
        } else {
            // USUÁRIO NOVO (PRÉ-REGISTRO)
            redirectUrl = "http://localhost:5173/finalizar-perfil";
        }

        // 5. REDIRECIONAMENTO FINAL (sem o token na URL, pois está no Cookie)
        response.sendRedirect(redirectUrl);
    }
}