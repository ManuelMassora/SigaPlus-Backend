package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.model.PerfilEstudante;
import com.sigaplus.sigaplus.model.Role;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.PerfilEstudanteRepository;
import com.sigaplus.sigaplus.repo.RoleRepository;
import com.sigaplus.sigaplus.repo.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomOidcUserService extends OidcUserService {
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PerfilEstudanteRepository perfilEstudanteRepository;

    public CustomOidcUserService(UsuarioRepository usuarioRepository,
                                 RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder,
                                 PerfilEstudanteRepository perfilEstudanteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.perfilEstudanteRepository = perfilEstudanteRepository;
    }

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String email = oidcUser.getEmail();
        String nome = oidcUser.getFullName();
        String urlImage = oidcUser.getPicture();

        String DOMAIN_PERMITIDO = "ucm.ac.mz";
        if (email == null || !email.endsWith("@" + DOMAIN_PERMITIDO)){
            throw new OAuth2AuthenticationException("Acesso negado: Apenas o domínio @" + DOMAIN_PERMITIDO + " é permitido.");
        }
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseGet(() -> {
                    Usuario novoUsuario = new Usuario();
                    novoUsuario.setEmail(email);
                    novoUsuario.setNome(nome);
                    novoUsuario.setAtivo(true);
                    String senha = UUID.randomUUID() + UUID.randomUUID().toString();
                    novoUsuario.setSenha(passwordEncoder.encode(senha));
                    Role roleEstudante = roleRepository.findFirstByName(Role.Values.ESTUDANTE.name())
                            .orElseThrow(() -> new RuntimeException("Role padrão não encontrado."));
                    novoUsuario.setRoles(Set.of(roleEstudante));
                    Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

                    PerfilEstudante perfil = new PerfilEstudante();
                    perfil.setUsuario(usuarioSalvo);
                    perfil.setNome(usuarioSalvo.getNome());
                    perfil.setUrlImagem(urlImage);
                    perfil.setCurso("Aguardando preenchimento");
                    perfil.setAnoAcademico("0");
                    perfilEstudanteRepository.save(perfil);

                    return usuarioSalvo;
                });
        Collection<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new CustomOidcUser(oidcUser, usuario, authorities);
    }
}