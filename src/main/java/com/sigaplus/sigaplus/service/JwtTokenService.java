package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.model.Usuario;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final Duration expirationDuration = Duration.ofHours(1);

    public JwtTokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(Usuario usuario) {
        Instant now = Instant.now();

        var roles = usuario.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("sigaplus")
                .subject(usuario.getEmail())
                .issuedAt(now)
                .expiresAt(now.plus(expirationDuration))
                .claim("authorities", roles)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String extractUsername(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getSubject();
        } catch (JwtException e) {
            return null;
        }
    }
}