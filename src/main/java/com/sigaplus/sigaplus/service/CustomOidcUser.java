package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;

public class CustomOidcUser extends DefaultOidcUser {

    private final Usuario usuario;

    public CustomOidcUser(OidcUser oidcUser, Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        // Chama o construtor pai com as authorities customizadas
        super(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
        this.usuario = usuario;
    }

    // Você pode adicionar métodos aqui para acessar seus dados customizados
    public Long getUserId() {
        return usuario.getId();
    }

    // Sobrescreve para usar o nome do DB, se necessário
    @Override
    public String getName() {
        return usuario.getNome();
    }
}