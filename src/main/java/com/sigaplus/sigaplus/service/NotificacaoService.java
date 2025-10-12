package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.NotificacaoDto;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.NotificacaoRepository;
import com.sigaplus.sigaplus.repo.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {
    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository, UsuarioRepository usuarioRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public NotificacaoDto buscar(JwtAuthenticationToken token, long notificacaoId) {
        var usuario = getUserByToken(token);
        var notificacao = notificacaoRepository.findByIdAndUsuarioId(notificacaoId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("mensagem nao econtrada"));

        if (!notificacao.isLida()) {
            notificacao.setLida(true);
            notificacaoRepository.save(notificacao);
        }
        return new NotificacaoDto(
                notificacao.getId(),
                notificacao.getTexto(),
                notificacao.getDataCriacao(),
                notificacao.isLida()
        );
    }

    public Page<NotificacaoDto> listar(JwtAuthenticationToken token, Pageable pageable) {
        var usuario = getUserByToken(token);
        var notificacoes = notificacaoRepository.findAllByUsuarioId(usuario.getId(), pageable);
        if (notificacoes.isEmpty()) {
            throw new EntityNotFoundException("nenhum notificacao");
        }
        return notificacoes.map(notificacao -> new NotificacaoDto(
                        notificacao.getId(),
                        notificacao.getTexto(),
                        notificacao.getDataCriacao(),
                        notificacao.isLida()
                )
        );
    }

    @Transactional
    public int marcarTodasComoLida(JwtAuthenticationToken token) {
        long usuarioId = getUserByToken(token).getId();
        return notificacaoRepository.marcarTodasComoLidas(usuarioId);
    }

    private Usuario getUserByToken(JwtAuthenticationToken token){
        return usuarioRepository.findById(Long.parseLong(token.getName()))
                .orElseThrow(() -> new InternalServerErrorException("usuario nao econtrado no token"));
    }
}
