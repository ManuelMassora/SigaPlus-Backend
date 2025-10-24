package com.sigaplus.sigaplus.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Filtro que move o JWT de um cookie chamado "access_token" para o cabeçalho Authorization.
 * Isso permite que o Resource Server (oauth2ResourceServer) autentique as chamadas
 * que chegam apenas com o cookie (como as feitas pelo frontend com 'withCredentials: true').
 */
public class CookieToHeaderFilter extends OncePerRequestFilter {

    private static final String COOKIE_NAME = "access_token";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Verifica se a requisição já tem um cabeçalho Authorization
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        // Verifica se a requisição não está autorizada E se há cookies
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            Cookie[] cookies = request.getCookies();
            String jwtFromCookie = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (COOKIE_NAME.equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                        jwtFromCookie = cookie.getValue();
                        break;
                    }
                }
            }

            // Se o token foi encontrado no cookie, reescreve a requisição
            if (jwtFromCookie != null) {
                // Cria um wrapper para modificar os cabeçalhos da requisição
                request = new HeaderOverrideRequestWrapper(request, jwtFromCookie);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Wrapper para sobrescrever o cabeçalho Authorization
     */
    private static class HeaderOverrideRequestWrapper extends HttpServletRequestWrapper {
        private final String jwtToken;

        public HeaderOverrideRequestWrapper(HttpServletRequest request, String jwtToken) {
            super(request);
            this.jwtToken = jwtToken;
        }

        @Override
        public String getHeader(String name) {
            if (AUTHORIZATION_HEADER.equalsIgnoreCase(name)) {
                return BEARER_PREFIX + jwtToken;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if (AUTHORIZATION_HEADER.equalsIgnoreCase(name)) {
                return Collections.enumeration(List.of(BEARER_PREFIX + jwtToken));
            }
            return super.getHeaders(name);
        }
    }
}