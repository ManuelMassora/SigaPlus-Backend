package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.EnviarMensagemDto;
import com.sigaplus.sigaplus.dto.MensagemDto;
import com.sigaplus.sigaplus.model.*;
import com.sigaplus.sigaplus.repo.ChatRepository;
import com.sigaplus.sigaplus.repo.MensagemRepository;
import com.sigaplus.sigaplus.repo.NotificacaoRepository;
import com.sigaplus.sigaplus.repo.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MensagemService {
    private final MensagemRepository mensagemRepository;
    private final ChatRepository chatRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoRepository notificacaoRepository;

    public MensagemService(MensagemRepository mensagemRepository,
                           ChatRepository chatRepository,
                           UsuarioRepository usuarioRepository,
                           NotificacaoRepository notificacaoRepository) {
        this.mensagemRepository = mensagemRepository;
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoRepository = notificacaoRepository;
    }

    @Transactional
    public MensagemDto enviarMensagem(JwtAuthenticationToken token, EnviarMensagemDto dto) {
        Long usuarioId = Long.parseLong(token.getName());
        Usuario estudante = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + usuarioId + " não encontrado."));
        Chat chat = chatRepository.findByUsuario1Id(usuarioId).orElse(null);

        if (chat == null) {
            Usuario psicologo = usuarioRepository.findFirstByRoles_Name(Role.Values.PSICOLOGO.name())
                    .orElseThrow(() -> new EntityNotFoundException("Nenhum psicólogo disponível."));

            chat = new Chat();
            chat.setUsuario1(estudante);
            chat.setPsicologo(psicologo);
            chat = chatRepository.save(chat);
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setTexto(dto.texto());
        mensagem.setEmissor(estudante);
        mensagem.setChat(chat);
        var mensagemSalva = mensagemRepository.save(mensagem);
        notificar(chat.getPsicologo(), "Você tem novas mensagens no chat de apoio");
        return new MensagemDto(
                mensagemSalva.getId(),
                mensagemSalva.getTexto(),
                mensagemSalva.getEmissor().getId(),
                mensagemSalva.getData()
        );
    }

    @Transactional
    public MensagemDto enviarMensagemPsicologo(JwtAuthenticationToken token, EnviarMensagemDto dto, long chatId) {
        Long usuarioId = Long.parseLong(token.getName());

        Usuario psicologo = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Psicólogo com ID " + usuarioId + " não encontrado."));

        boolean isPsicologo = psicologo.getRoles().stream()
                .anyMatch(role -> Objects.equals(role.getName(), Role.Values.PSICOLOGO.name()));
        if (!isPsicologo) {
            throw new IllegalArgumentException("Usuário com ID " + usuarioId + " não possui o perfil de PSICÓLOGO.");
        }

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat com ID " + chatId + " não encontrado."));

        if (chat.getPsicologo().getId() != psicologo.getId()) {
            throw new SecurityException("Você não tem permissão para enviar mensagem neste chat.");
        }

        if (dto.texto() == null || dto.texto().trim().isEmpty()) {
            throw new IllegalArgumentException("O texto da mensagem não pode estar vazio.");
        }
        Mensagem mensagem = new Mensagem();
        mensagem.setTexto(dto.texto().trim());
        mensagem.setEmissor(psicologo);
        mensagem.setChat(chat);
        var mensagemSalva = mensagemRepository.save(mensagem);

        notificar(chat.getUsuario1(), "Você tem novas mensagens no chat de apoio");
        return new MensagemDto(
                mensagemSalva.getId(),
                mensagemSalva.getTexto(),
                mensagemSalva.getEmissor().getId(),
                mensagemSalva.getData()
        );
    }

    public List<MensagemDto> listarMensagensDoEstudante(JwtAuthenticationToken token) {
        Long estudanteId = Long.parseLong(token.getName());
        usuarioRepository.findById(estudanteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante com ID " + estudanteId + " não encontrado."));

        Chat chat = chatRepository.findByUsuario1Id(estudanteId)
                .orElseThrow(() -> new EntityNotFoundException("Chat não encontrado para o estudante com ID " + estudanteId + "."));
        var mensagens = mensagemRepository.findByChatIdAndRemovidoIsFalseOrderByDataDesc(chat.getId());
        return mensagens.stream().map(mensagem -> new MensagemDto(
                mensagem.getId(),
                mensagem.getTexto(),
                mensagem.getEmissor().getId(),
                mensagem.getData()
        )).toList();
    }

    public List<MensagemDto> listarMensagensDoPsicologo(JwtAuthenticationToken token, long chatId) {
        Long psicologoId = Long.parseLong(token.getName());
        Usuario psicologo = usuarioRepository.findById(psicologoId)
                .orElseThrow(() -> new EntityNotFoundException("Psicólogo com ID " + psicologoId + " não encontrado."));

        boolean isPsicologo = psicologo.getRoles().stream()
                .anyMatch(role -> Objects.equals(role.getName(), Role.Values.PSICOLOGO.name()));

        if (!isPsicologo) {
            throw new SecurityException("Acesso negado. Usuário com ID " + psicologoId + " não possui o perfil de PSICÓLOGO.");
        }

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat com ID " + chatId + " não encontrado."));

        if (chat.getPsicologo().getId() != psicologo.getId()) {
            throw new SecurityException("Você não tem permissão para listar mensagem neste chat.");
        }

        var mensagens = mensagemRepository.findByChatIdAndRemovidoIsFalseOrderByDataDesc(chat.getId());
        return mensagens.stream().map(mensagem -> new MensagemDto(
                mensagem.getId(),
                mensagem.getTexto(),
                mensagem.getEmissor().getId(),
                mensagem.getData()
        )).toList();
    }

    private void notificar(Usuario destinatario, String texto) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(destinatario);
        notificacao.setTexto(texto);
        notificacaoRepository.save(notificacao);
    }
}