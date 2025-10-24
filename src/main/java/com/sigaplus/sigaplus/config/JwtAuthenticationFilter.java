package com.sigaplus.sigaplus.config;

import com.sigaplus.sigaplus.service.JwtTokenService;
import com.sigaplus.sigaplus.repo.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie; // Importar Cookie
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(JwtTokenService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException, IOException {

        String jwt = null;
        String authHeader = request.getHeader("Authorization");

        // 1. Tenta obter o token do Cookie (PRIORIDADE)
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("AUTH-TOKEN".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        // 2. Se o token não foi encontrado no Cookie, tenta obter do Header Bearer
        if (jwt == null && authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }


        // 3. Se o token ainda for nulo, permita que a cadeia de filtros continue (não autenticado)
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // --- INÍCIO DA LÓGICA DE VALIDAÇÃO (Se o JWT foi encontrado) ---

        // Garante que o usuário ainda não esteja autenticado
        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            final String userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null) {
                // Tente a validação do token com try-catch para evitar falhas silenciosas
                try {
                    // Busca o usuário no DB (UserDetails)
                    UserDetails userDetails = (UserDetails) usuarioRepository.findByEmail(userEmail)
                            .orElse(null);

                    // Se o token for válido
                    if (userDetails != null && userEmail.equals(userDetails.getUsername())) {

                        // Cria e configura o objeto de autenticação
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, // Principal = Objeto Usuario (UserDetails)
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        // Atualiza o SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (Exception e) {
                    // Se o token for inválido (expirado, assinado incorretamente, etc.),
                    // a autenticação falha, mas a cadeia de filtros continua.
                    // O Spring Security tratará isso como uma requisição não autenticada.
                    System.err.println("Token JWT inválido ou expirado: " + e.getMessage());
                }
            }
        }

        // Permite que a requisição prossiga
        filterChain.doFilter(request, response);
    }
}