package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.ComentarioDto;
import com.sigaplus.sigaplus.dto.CriarComentario;
import com.sigaplus.sigaplus.dto.PerfilEstudanteDto;
import com.sigaplus.sigaplus.model.Comentario;
import com.sigaplus.sigaplus.model.Notificacao;
import com.sigaplus.sigaplus.model.Postagem;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {
    private final ComentarioRepository comentarioRepository;
    private final PostagemRepository postagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilEstudanteRepository perfilEstudanteRepository;
    private final NotificacaoRepository notificacaoRepository;

    public ComentarioService(ComentarioRepository comentarioRepository,
                             PostagemRepository postagemRepository,
                             UsuarioRepository usuarioRepository,
                             PerfilEstudanteRepository perfilEstudanteRepository,
                             NotificacaoRepository notificacaoRepository) {
        this.comentarioRepository = comentarioRepository;
        this.postagemRepository = postagemRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilEstudanteRepository = perfilEstudanteRepository;
        this.notificacaoRepository = notificacaoRepository;
    }

    @Transactional
    public void criar(JwtAuthenticationToken token, long postagemId, CriarComentario dto) {
        var usuario = getUserByToken(token);
        var postagem = buscarPostagemAtiva(postagemId);

        if (dto.conteudo() == null || dto.conteudo().isBlank()) {
            throw new IllegalArgumentException("O conteúdo do comentário não pode estar vazio");
        }

        Comentario comentario = new Comentario();
        comentario.setUsuario(usuario);
        comentario.setConteudo(dto.conteudo());
        comentario.setPostagem(postagem);

        if (dto.comentarioId() != null) {
            var comentarioRespondido = buscarComentarioAtivo(dto.comentarioId());

            String texto = String.format("%s respondeu ao seu comentário no topico '%s'",
                    usuario.getNome(), postagem.getTopico().getTitulo());
            notificarUsuario(comentarioRespondido.getUsuario(), texto);
            comentario.setComentarioRespondido(comentarioRespondido);
        }
        comentarioRepository.save(comentario);
    }

    private void notificarUsuario(Usuario destinatario, String texto) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(destinatario);
        notificacao.setTexto(texto);
        notificacaoRepository.save(notificacao);
    }

    private Postagem buscarPostagemAtiva(long id) {
        return postagemRepository.findByIdAndRemovidoIsFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Postagem não encontrada"));
    }

    private Comentario buscarComentarioAtivo(long id) {
        return comentarioRepository.findByIdAndRemovidoIsFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("O comentário respondido não existe"));
    }

    @Transactional
    public void remover(JwtAuthenticationToken token, long comentarioId) {
        var usuario = getUserByToken(token);
        var comentario = comentarioRepository.findByIdAndUsuarioIdAndRemovidoIsFalse(comentarioId, usuario.getId())
                .orElseThrow(() -> new ForbiddenException("sem permissao para apagar esse comentario"));
        comentario.setRemovido(true);
        comentarioRepository.save(comentario);
    }

    @Transactional
    public void editar(JwtAuthenticationToken token, long comentarioId, CriarComentario dto) {
        var usuario = getUserByToken(token);
        var comentario = comentarioRepository.findByIdAndUsuarioIdAndRemovidoIsFalse(comentarioId, usuario.getId())
                .orElseThrow(() -> new ForbiddenException("sem permissao para editar esse comentario"));
        comentario.setConteudo(dto.conteudo());
        if (dto.comentarioId() != null) {
            var comentarioRespondido = comentarioRepository.findByIdAndRemovidoIsFalse(dto.comentarioId())
                    .orElseThrow(() -> new EntityNotFoundException("O comentário respondido não existe"));
            comentario.setComentarioRespondido(comentarioRespondido);
        }
        comentarioRepository.save(comentario);
    }

    public ComentarioDto buscar(long id) {
        var comentario = comentarioRepository.findByIdAndRemovidoIsFalse(id)
                .orElseThrow(() -> new  EntityNotFoundException("comentario nao econtrado"));
        return getComentarioDto(comentario);
    }

    public Page<ComentarioDto> listarPorPostagemId(long postagemId, Pageable pageable) {
        var comentarios = comentarioRepository.findAllByPostagemIdAndRemovidoIsFalseOrderByDataCriacaoDesc(postagemId, pageable);
        if (comentarios.isEmpty()) {
            throw new EntityNotFoundException("nenhum comentario ainda");
        }
        return comentarios.map(this::getComentarioDto);
    }

    private ComentarioDto getComentarioDto(Comentario comentario) {
        var perfilEstudante = perfilEstudanteRepository.findByUsuarioId(comentario.getUsuario().getId())
                .orElseThrow(() -> new InternalServerErrorException("ocorreu um erro inesperado"));
        String nomeUsuarioRespondido = null;
        if (comentario.getComentarioRespondido() != null && comentario.getComentarioRespondido().getUsuario() != null) {
            nomeUsuarioRespondido = comentario.getComentarioRespondido().getUsuario().getNome();
        }
        return new ComentarioDto(
                comentario.getId(),
                comentario.getConteudo(),
                comentario.getDataCriacao(),
                nomeUsuarioRespondido,
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

    private Usuario getUserByToken(JwtAuthenticationToken token){
        return usuarioRepository.findById(Long.parseLong(token.getName()))
                .orElseThrow(() -> new InternalServerErrorException("usuario nao econtrado no token"));
    }
}