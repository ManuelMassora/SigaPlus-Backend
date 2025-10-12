package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.CriarPostagem;
import com.sigaplus.sigaplus.dto.PerfilEstudanteDto;
import com.sigaplus.sigaplus.dto.PostagemDto;
import com.sigaplus.sigaplus.model.Postagem;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class PostagemService {
    private final PostagemRepository postagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final PostagemCurtidaRepository postagemCurtidaRepository;
    private final TopicoRepository topicoRepository;
    private final PerfilEstudanteRepository perfilEstudanteRepository;

    public PostagemService(PostagemRepository postagemRepository,
                           UsuarioRepository usuarioRepository,
                           PostagemCurtidaRepository postagemCurtidaRepository,
                           TopicoRepository topicoRepository,
                           PerfilEstudanteRepository perfilEstudanteRepository) {
        this.postagemRepository = postagemRepository;
        this.usuarioRepository = usuarioRepository;
        this.postagemCurtidaRepository = postagemCurtidaRepository;
        this.topicoRepository = topicoRepository;
        this.perfilEstudanteRepository = perfilEstudanteRepository;
    }

    @Transactional
    public void criar(JwtAuthenticationToken token, long topicoId, CriarPostagem dto) {
        var usuario = getUserByToken(token);
        var topico = topicoRepository.findByIdAndRemovidoIsFalse(topicoId)
                .orElseThrow(() -> new EntityNotFoundException("Topico nao existente"));
        Postagem postagem = new Postagem();
        postagem.setConteudo(dto.conteudo());
        postagem.setTopico(topico);
        postagem.setUsuario(usuario);
        postagemRepository.save(postagem);
    }

    @Transactional
    public void remover(JwtAuthenticationToken token, long postagemId) {
        long usuarioId = getUserByToken(token).getId();
        Postagem postagem = postagemRepository.findByIdAndUsuarioIdAndRemovidoIsFalse(postagemId, usuarioId)
                .orElseThrow(() -> new ForbiddenException("sem permissao para apagar esse topico"));
        postagem.setRemovido(true);
        postagemRepository.save(postagem);
    }

    @Transactional
    public void editar(JwtAuthenticationToken token, long postagemId, CriarPostagem dto) {
        long usuarioId = getUserByToken(token).getId();
        Postagem postagem = postagemRepository.findByIdAndUsuarioIdAndRemovidoIsFalse(postagemId, usuarioId)
                .orElseThrow(() -> new ForbiddenException("sem permissao para editar esse topico"));
        postagem.setConteudo(dto.conteudo());
        postagemRepository.save(postagem);
    }

    public PostagemDto buscar(long postagemId) {
        Postagem postagem = postagemRepository.findByIdAndRemovidoIsFalse(postagemId)
                .orElseThrow(() -> new EntityNotFoundException("postagem nao econtrada"));
        var perfilEstudante = perfilEstudanteRepository.findByUsuarioId(postagem.getUsuario().getId())
                .orElseThrow(() -> new EntityNotFoundException("erro inesperado"));
        return new PostagemDto(
                postagem.getId(),
                postagem.getConteudo(),
                postagem.getDataCriacao(),
                postagem.getTopico().getId(),
                new PerfilEstudanteDto(
                        perfilEstudante.getNome(),
                        perfilEstudante.getCurso(),
                        perfilEstudante.getAnoAcademico(),
                        perfilEstudante.getSobreMim(),
                        perfilEstudante.getEspecialidadeEm(),
                        perfilEstudante.getUrlImagem()
                )
        );
    }

    public Page<PostagemDto> listarPorTopicoId(long topicoId, Pageable pageable) {
        var postagens = postagemRepository.findAllByTopicoIdAndRemovidoIsFalse(topicoId, pageable);
        if (postagens.isEmpty()) {
            throw new EntityNotFoundException("nenhuma postagem econtrada");
        }
        return postagens.map(postagem -> {
            var perfilEstudante = perfilEstudanteRepository.findByUsuarioId(postagem.getUsuario().getId())
                    .orElseThrow(() -> new EntityNotFoundException("erro inesperado"));
            return new PostagemDto(
                    postagem.getId(),
                    postagem.getConteudo(),
                    postagem.getDataCriacao(),
                    postagem.getTopico().getId(),
                    new PerfilEstudanteDto(
                            perfilEstudante.getNome(),
                            perfilEstudante.getCurso(),
                            perfilEstudante.getAnoAcademico(),
                            perfilEstudante.getSobreMim(),
                            perfilEstudante.getEspecialidadeEm(),
                            perfilEstudante.getUrlImagem()
                    )
            );
        });
    }

    private Usuario getUserByToken(JwtAuthenticationToken token) {
        Usuario usuario = usuarioRepository.findById(Long.parseLong(token.getName()))
                .orElseThrow(() -> new EntityNotFoundException("Usuario nao econtrado no Token"));
        return usuario;
    }
}
