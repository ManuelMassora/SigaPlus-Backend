package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.CriarPostagem;
import com.sigaplus.sigaplus.dto.PerfilEstudanteDto;
import com.sigaplus.sigaplus.dto.PostagemDto;
import com.sigaplus.sigaplus.model.Postagem;
import com.sigaplus.sigaplus.model.PostagemCurtida;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public void criar(Usuario usuario, long topicoId, CriarPostagem dto) {
        var topico = topicoRepository.findByIdAndRemovidoIsFalse(topicoId)
                .orElseThrow(() -> new EntityNotFoundException("Topico nao existente"));
        Postagem postagem = new Postagem();
        postagem.setConteudo(dto.conteudo());
        postagem.setTopico(topico);
        postagem.setUsuario(usuario);
        postagemRepository.save(postagem);
    }

    @Transactional
    public void remover(Usuario usuario, long postagemId) {
        Postagem postagem = postagemRepository.findByIdAndUsuarioIdAndRemovidoIsFalse(postagemId, usuario.getId())
                .orElseThrow(() -> new ForbiddenException("sem permissao para apagar esse topico"));
        postagem.setRemovido(true);
        postagemRepository.save(postagem);
    }

    @Transactional
    public void editar(Usuario usuario, long postagemId, CriarPostagem dto) {
        Postagem postagem = postagemRepository.findByIdAndUsuarioIdAndRemovidoIsFalse(postagemId, usuario.getId())
                .orElseThrow(() -> new ForbiddenException("sem permissao para editar esse topico"));
        postagem.setConteudo(dto.conteudo());
        postagemRepository.save(postagem);
    }

    public PostagemDto buscar(long postagemId) {
        Postagem postagem = postagemRepository.findByIdAndRemovidoIsFalse(postagemId)
                .orElseThrow(() -> new EntityNotFoundException("postagem nao econtrada"));
        return getPostagemDto(postagem);
    }

    public Page<PostagemDto> listarPorTopicoId(long topicoId, Pageable pageable) {
        return getPostagemDtos(pageable, topicoId);
    }

    public Page<PostagemDto> listarMinhasPostagens(Usuario usuario, Pageable pageable) {
        return getPostagemDtos(pageable, usuario.getId());
    }

    private PostagemDto getPostagemDto(Postagem postagem) {
        var perfilEstudante = perfilEstudanteRepository.findByUsuarioId(postagem.getUsuario().getId())
                .orElseThrow(() -> new EntityNotFoundException("erro inesperado"));
        return new PostagemDto(
                postagem.getId(),
                postagem.getConteudo(),
                postagem.getDataCriacao(),
                postagem.getCurtidas(),
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

    private Page<PostagemDto> getPostagemDtos(Pageable pageable, long usuarioId) {
        var postagens = postagemRepository.findAllByTopicoIdAndRemovidoIsFalse(usuarioId, pageable);
        if (postagens.isEmpty()) {
            throw new EntityNotFoundException("nenhuma postagem econtrada");
        }
        return postagens.map(this::getPostagemDto);
    }

    @Transactional
    public void curtir(Usuario usuario, long postagemId) {
        Postagem postagem = postagemRepository.findByIdAndRemovidoIsFalse(postagemId)
                .orElseThrow(() -> new EntityNotFoundException("postagem nao econtrada"));
        if (postagemCurtidaRepository.existsByPostagemIdAndUsuarioId(postagem.getId(), usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já contém uma curtida");
        }
        PostagemCurtida postagemCurtida = new PostagemCurtida();
        postagemCurtida.setPostagem(postagem);
        postagemCurtida.setUsuario(usuario);
        postagemCurtidaRepository.save(postagemCurtida);
    }
}
