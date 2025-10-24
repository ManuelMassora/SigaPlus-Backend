package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.CriarTopico;
import com.sigaplus.sigaplus.dto.PerfilEstudanteDto;
import com.sigaplus.sigaplus.dto.TopicoDto;
import com.sigaplus.sigaplus.model.*;
import com.sigaplus.sigaplus.repo.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {
    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TopicoCurtidaRepository topicoCurtidaRepository;
    private final TopicoVisualizacaoRepository topicoVisualizacaoRepository;
    private final PerfilEstudanteRepository perfilEstudanteRepository;

    public TopicoService(TopicoRepository topicoRepository,
                         UsuarioRepository usuarioRepository,
                         TopicoCurtidaRepository topicoCurtidaRepository,
                         TopicoVisualizacaoRepository topicoVisualizacaoRepository,
                         PerfilEstudanteRepository perfilEstudanteRepository) {
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.topicoCurtidaRepository = topicoCurtidaRepository;
        this.topicoVisualizacaoRepository = topicoVisualizacaoRepository;
        this.perfilEstudanteRepository = perfilEstudanteRepository;
    }

    @Transactional
    public void criarTopico(Usuario usuario, CriarTopico dto) {
        Topico topico = new Topico();
        topico.setUsuario(usuario);
        topico.setTitulo(dto.titulo());
        topico.setDescricao(dto.descricao());
        topicoRepository.save(topico);
    }

    @Transactional
    public void removerTopico(Usuario usuario, long topicoId){
        Topico topico = topicoRepository.findFirstByIdAndUsuarioIdAndRemovidoIsFalse(topicoId, usuario.getId())
                .orElseThrow(() -> new ForbiddenException("sem permissao para apagar esse topico"));
        topico.setRemovido(true);
        topicoRepository.save(topico);
    }

    @Transactional
    public TopicoDto buscarTopico(Usuario usuario, long topicoId) {
        Topico topico = topicoRepository.findByIdAndRemovidoIsFalse(topicoId)
                .orElseThrow(() -> new EntityNotFoundException("topico nao econtrado"));
        if (!topicoVisualizacaoRepository.existsByTopicoIdAndUsuarioId(topico.getId(), usuario.getId())) {
            TopicoVisualizacao visualizacao = new TopicoVisualizacao();
            visualizacao.setTopico(topico);
            visualizacao.setUsuario(usuario);
            topicoVisualizacaoRepository.save(visualizacao);
        }
        var perfilEstudante = perfilEstudanteRepository.findByUsuarioId(topico.getUsuario().getId())
                .orElseThrow(() -> new EntityNotFoundException("erro inesperado"));
        return new TopicoDto(
                topico.getId(),
                topico.getTitulo(),
                topico.getDescricao(),
                topico.getTotalCurtidas(),
                topico.getTotalVisualizacoes(),
                topico.getDataCriacao(),
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

    public Page<TopicoDto> listarTopicos(String titulo, Pageable pageable) {
        if (titulo == null || titulo.isBlank()) {
            titulo = "";
        }
        Page<Topico> topicos = topicoRepository.findAllByTituloContainingAndRemovidoIsFalse(titulo, pageable);
        return getTopicoDtos(topicos);
    }

    public Page<TopicoDto> listarMeusTopicos(Usuario usuario, Pageable pageable) {
        Page<Topico> topicos = topicoRepository.findByUsuarioIdAndRemovidoFalse(usuario.getId(), pageable);
        return getTopicoDtos(topicos);
    }

    public Page<TopicoDto> listarTopicosCurtidos(Usuario usuario, Pageable pageable) {
        Page<Topico> topicos = topicoRepository.findTopicosCurtidosPorUsuario(usuario.getId(), pageable);
        return getTopicoDtos(topicos);
    }

    private Page<TopicoDto> getTopicoDtos(Page<Topico> topicos) {
        return topicos.map(topico -> {
            PerfilEstudante perfilEstudante = perfilEstudanteRepository.findByUsuarioId(topico.getUsuario().getId())
                    .orElseThrow(() -> new EntityNotFoundException("erro inesperado"));

            return new TopicoDto(
                    topico.getId(),
                    topico.getTitulo(),
                    topico.getDescricao(),
                    topico.getTotalCurtidas(),
                    topico.getTotalVisualizacoes(),
                    topico.getDataCriacao(),
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

    @Transactional
    public void curtirTopico(Usuario usuario, long topicoId){
        Topico topico = topicoRepository.findById(topicoId)
                .orElseThrow(() -> new EntityNotFoundException("topico nao econtrado"));
        if (topicoCurtidaRepository.existsByTopicoIdAndUsuarioId(topico.getId(), usuario.getId())) {
            throw new BadRequestException("já curtiu este tópico.");
        }
        TopicoCurtida curtida = new TopicoCurtida();
        curtida.setTopico(topico);
        curtida.setUsuario(usuario);
        topicoCurtidaRepository.save(curtida);
    }
}
