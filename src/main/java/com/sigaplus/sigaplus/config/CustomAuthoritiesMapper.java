//package com.sigaplus.sigaplus.config;
//
//import com.sigaplus.sigaplus.repo.UsuarioRepository;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
//import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//@Component
//public class CustomAuthoritiesMapper implements GrantedAuthoritiesMapper {
//
//    private final UsuarioRepository usuarioRepository;
//
//    public CustomAuthoritiesMapper(UsuarioRepository usuarioRepository) {
//        this.usuarioRepository = usuarioRepository;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
//        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//
//        // Encontra a autoridade OIDC/OAuth2 para obter o email/username
//        authorities.forEach(authority -> {
//            if (authority instanceof OidcUserAuthority oidcAuth) {
//                // Pega o email para buscar o usuário no seu DB
//                String email = oidcAuth.getIdToken().getClaimAsString("email");
//
//                // BUSCA NO DB e adiciona as Roles do DB
//                usuarioRepository.findByEmail(email).ifPresent(usuario -> {
//                    usuario.getRoles().forEach(role -> {
//                        mappedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
//                    });
//                });
//                mappedAuthorities.add(oidcAuth); // Mantém a autoridade OIDC original
//            } else if (authority instanceof OAuth2UserAuthority oauth2Auth) {
//                // Lógica similar para OAuth2UserAuthority se necessário
//            }
//        });
//
//        if (mappedAuthorities.isEmpty()) {
//            throw new OAuth2AuthenticationException("Não foi possível mapear autoridade para o usuário.");
//        }
//
//        return mappedAuthorities;
//    }
//}