package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.CriarEstudanteDto;
import com.sigaplus.sigaplus.dto.CriarUsuarioDto;
import com.sigaplus.sigaplus.dto.UsuarioDto;
import com.sigaplus.sigaplus.model.PerfilEstudante;
import com.sigaplus.sigaplus.model.Role;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.PerfilEstudanteRepository;
import com.sigaplus.sigaplus.repo.RoleRepository;
import com.sigaplus.sigaplus.repo.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PerfilEstudanteRepository perfilEstudanteRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder cryptPasswordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilEstudanteRepository perfilEstudanteRepository, RoleRepository roleRepository, PasswordEncoder cryptPasswordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilEstudanteRepository = perfilEstudanteRepository;
        this.roleRepository = roleRepository;
        this.cryptPasswordEncoder = cryptPasswordEncoder;
    }

    @Transactional
    public void atualizarPerfilEstudante(Usuario usuarioToken, CriarEstudanteDto dto) {
//        if (dto.curso() == null || dto.anoAcademico() == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos obrigatórios (curso e ano acadêmico) não preenchidos.");
//        }

        PerfilEstudante perfilEstudante = perfilEstudanteRepository.findByUsuarioId(usuarioToken.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "PerfilEstudante não encontrado para o usuário. Inconsistência JIT."
                ));
        perfilEstudante.setCurso(dto.curso());
        perfilEstudante.setAnoAcademico(dto.anoAcademico());
        perfilEstudante.setSobreMim(dto.sobreMim());
        perfilEstudanteRepository.save(perfilEstudante);
    }

    @Transactional
    public void CriarUsuarioInterno(CriarUsuarioDto dto) {
        switch (dto.role()) {
            case "admin" -> criarUsuario(dto, Role.Values.ADMIN.name());
            case "profissional" -> criarUsuario(dto, Role.Values.PROFISSIONAL.name());
            case "psicologo" -> criarUsuario(dto, Role.Values.PSICOLOGO.name());
            case "ssr" -> criarUsuario(dto, Role.Values.SSR.name());
            case "estudante" -> criarUsuario(dto, Role.Values.ESTUDANTE.name());
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role nao identificada");
        }
    }

    public UsuarioDto buscar(long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("usuario econtrado"));
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.isAtivo()
        );
    }

    public Page<UsuarioDto> listar(Pageable pageable) {
        var usuarios = usuarioRepository.findAll(pageable);
        if (usuarios.isEmpty()) {
            throw new NotFoundException("nenhum usuario na lista");
        }
        return usuarios.map(usuario -> new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.isAtivo()
                )
        );
    }

    private void criarUsuario(CriarUsuarioDto dto, String rolename) {
        var role = roleRepository.findByName(Role.Values.valueOf(rolename).name());
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role nao existe no banco de dados");
        }
        if (dto.nome() == null || dto.email() == null || dto.senha() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos obrigatorios nao preeechidos");
        }
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Email ja existe");
        }
        var usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(cryptPasswordEncoder.encode(dto.senha()));
        usuario.setRoles(Set.of(role));
        try {
          usuarioRepository.save(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Ouve um erro ao criar usuario: " + e.getMessage(), e);
        }
    }
}