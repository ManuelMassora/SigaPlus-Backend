package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.NotificacaoDto;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.NotificacaoRepository;
import com.sigaplus.sigaplus.repo.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public NotificacaoDto buscar(Usuario usuario, long notificacaoId) {
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
                notificacao.isLida(),
                notificacao.getTipo().toString(),
                notificacao.getReferenciaId()
        );
    }

    public Page<NotificacaoDto> listar(Usuario usuario, Pageable pageable) {
        var notificacoes = notificacaoRepository.findAllByUsuarioId(usuario.getId(), pageable);
        if (notificacoes.isEmpty()) {
            throw new EntityNotFoundException("nenhuma notificacao");
        }
        return notificacoes.map(notificacao -> new NotificacaoDto(
                        notificacao.getId(),
                        notificacao.getTexto(),
                        notificacao.getDataCriacao(),
                        notificacao.isLida(),
                        notificacao.getTipo().toString(),
                        notificacao.getReferenciaId()
                )
        );
    }

    @Transactional
    public int marcarTodasComoLida(Usuario usuario) {
        return notificacaoRepository.marcarTodasComoLidas(usuario.getId());
    }
}
