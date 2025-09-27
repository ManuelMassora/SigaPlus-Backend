package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.CriarEstudanteDto;
import com.sigaplus.sigaplus.dto.CriarUsuarioDto;
import com.sigaplus.sigaplus.model.PerfilEstudante;
import com.sigaplus.sigaplus.model.Role;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.PerfilEstudanteRepository;
import com.sigaplus.sigaplus.repo.RoleRepository;
import com.sigaplus.sigaplus.repo.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PerfilEstudanteRepository perfilEstudanteRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder cryptPasswordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilEstudanteRepository perfilEstudanteRepository, RoleRepository roleRepository, BCryptPasswordEncoder cryptPasswordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilEstudanteRepository = perfilEstudanteRepository;
        this.roleRepository = roleRepository;
        this.cryptPasswordEncoder = cryptPasswordEncoder;
    }

    @Transactional
    public void CriarEstudante(CriarEstudanteDto dto) {
        var role = roleRepository.findByName(Role.Values.ESTUDANTE.name());
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ouve um erro ao criar usuario");
        }
        if (dto.usuario() == null || dto.curso() == null || dto.anoAcademico() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos obrigatorios nao preeechidos");
        }
        if (usuarioRepository.findByEmail(dto.usuario().email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Email ja existe");
        }
        var usuario = new Usuario();
        usuario.setNome(dto.usuario().nome());
        usuario.setEmail(dto.usuario().email());
        usuario.setSenha(cryptPasswordEncoder.encode(dto.usuario().senha()));
        usuario.setRoles(Set.of(role));
        try {
            usuarioRepository.save(usuario);
            var perfilEstudante = new  PerfilEstudante();
            perfilEstudante.setUsuario(usuario);
            perfilEstudante.setNome(usuario.getNome());
            perfilEstudante.setCurso(dto.curso());
            perfilEstudante.setAnoAcademico(dto.anoAcademico());
            perfilEstudante.setSobreMim(dto.sobreMim());
            perfilEstudante.setEspecialidadeEm(dto.especialidadeEm());
            perfilEstudanteRepository.save(perfilEstudante);
        } catch (Exception e) {
            throw new RuntimeException("Ouve um erro ao criar usuario: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void CriarUsuarioInterno(CriarUsuarioDto dto) {
        switch (dto.role()) {
            case "admin":
                criarUsuario(dto, Role.Values.ADMIN.name());
                break;
            case "profissional":
                criarUsuario(dto, Role.Values.PROFISSIONAL.name());
                break;
            case "psicologo":
                criarUsuario(dto, Role.Values.PSICOLOGO.name());
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role nao identificada");
        }
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