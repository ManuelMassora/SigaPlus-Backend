package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.EnviarMensagemDto;
import com.sigaplus.sigaplus.dto.MensagemDto;
import com.sigaplus.sigaplus.model.*;
import com.sigaplus.sigaplus.repo.*;
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
    private final ChatParticipanteRepository chatParticipanteRepository;

    public MensagemService(MensagemRepository mensagemRepository,
                           ChatRepository chatRepository,
                           UsuarioRepository usuarioRepository,
                           NotificacaoRepository notificacaoRepository,
                           ChatParticipanteRepository chatParticipanteRepository) {
        this.mensagemRepository = mensagemRepository;
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.chatParticipanteRepository = chatParticipanteRepository;
    }

    @Transactional
    public MensagemDto enviarMensagem(Usuario usuario, EnviarMensagemDto dto, String tipo) {
        Long usuarioId = usuario.getId();

        Usuario emissor = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + usuarioId + " não encontrado."));

        TipoChat tipoChat = switch (tipo) {
            case "PSICOLOGO" -> TipoChat.PSICOLOGICO;
            case "SSR" -> TipoChat.SSR;
            default -> throw new IllegalArgumentException("Tipo de suporte inválido: " + tipo);
        };

        Chat chat = chatRepository.findByUsuario1IdAndTipoChat(usuarioId, tipoChat).orElse(null);

        Long destinatarioId;
        Usuario usuarioApoio;

        if (chat == null) {
            usuarioApoio = usuarioRepository.findFirstByRoles_Name(tipo)
                    .orElseThrow(() -> new EntityNotFoundException("Nenhum usuário de apoio (" + tipo + ") disponível."));

            destinatarioId = usuarioApoio.getId();

            Chat newChat = new Chat();
            newChat.setUsuario1(emissor);
            newChat.setPsicologo(usuarioApoio);
            newChat.setTipoChat(tipoChat);
            chat = chatRepository.save(newChat);

            ChatParticipante estudanteParticipante = new ChatParticipante();
            estudanteParticipante.setChat(chat);
            estudanteParticipante.setUsuario(emissor);

            ChatParticipante apoioParticipante = new ChatParticipante();
            apoioParticipante.setChat(chat);
            apoioParticipante.setUsuario(usuarioApoio);
            apoioParticipante.setMensagensNaoLidas(1);

            chatParticipanteRepository.save(estudanteParticipante);
            chatParticipanteRepository.save(apoioParticipante);
        } else {
            if (chat.getUsuario1().equals(emissor)) {
                destinatarioId = chat.getPsicologo().getId();
                usuarioApoio = chat.getPsicologo();
            } else {
                destinatarioId = chat.getUsuario1().getId();
                usuarioApoio = chat.getUsuario1();
            }
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setTexto(dto.texto());
        mensagem.setEmissor(emissor);
        mensagem.setChat(chat);
        var mensagemSalva = mensagemRepository.save(mensagem);

        ChatParticipante participanteDestinatario = chatParticipanteRepository
                .findByChatIdAndUsuarioId(chat.getId(), destinatarioId)
                .orElseThrow(() -> new IllegalStateException("Erro interno: Participante destinatário não encontrado no chat."));

        participanteDestinatario.setMensagensNaoLidas(participanteDestinatario.getMensagensNaoLidas() + 1);
        chatParticipanteRepository.save(participanteDestinatario);

        Usuario destinatario = usuarioRepository.findById(destinatarioId).orElse(null);
        if (destinatario != null) {
            notificar(destinatario, "Você tem novas mensagens no chat de apoio", chat.getId());
        }

        return new MensagemDto(
                mensagemSalva.getId(),
                mensagemSalva.getTexto(),
                mensagemSalva.getEmissor().getId(),
                mensagemSalva.getData()
        );
    }

    @Transactional
    public MensagemDto enviarMensagemPsicologo(Usuario usuarioApoioId, EnviarMensagemDto dto, long chatId) {

        Usuario usuarioApoio = usuarioRepository.findById(usuarioApoioId.getId())
                .orElseThrow(() -> new EntityNotFoundException("Psicólogo com ID " + usuarioApoioId + " não encontrado."));

        boolean isUsuarioDeApoio = usuarioApoio.getRoles().stream()
                .anyMatch(role -> Objects.equals(role.getName(), Role.Values.PSICOLOGO.name()) ||
                        Objects.equals(role.getName(), Role.Values.SSR.name()));

        if (!isUsuarioDeApoio) {
            throw new SecurityException("Acesso negado. Usuário com ID " + usuarioApoioId + " não possui o perfil de PSICÓLOGO ou SSR.");
        }

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat com ID " + chatId + " não encontrado."));

        if (chat.getPsicologo().getId() != usuarioApoio.getId()) {
            throw new SecurityException("Você não tem permissão para enviar mensagem neste chat.");
        }

        if (dto.texto() == null || dto.texto().trim().isEmpty()) {
            throw new IllegalArgumentException("O texto da mensagem não pode estar vazio.");
        }
        Mensagem mensagem = new Mensagem();
        mensagem.setTexto(dto.texto().trim());
        mensagem.setEmissor(usuarioApoio);
        mensagem.setChat(chat);
        var mensagemSalva = mensagemRepository.save(mensagem);

        ChatParticipante participanteDestinatario = chatParticipanteRepository.findByChatIdAndUsuarioId(chat.getId(), chat.getUsuario1().getId())
                .orElseThrow(() -> new IllegalStateException("Participante destinatário não encontrado no chat."));

        participanteDestinatario.setMensagensNaoLidas(participanteDestinatario.getMensagensNaoLidas() + 1);
        chatParticipanteRepository.save(participanteDestinatario);

        notificar(chat.getUsuario1(), "Você tem novas mensagens no chat de apoio", chatId);
        return new MensagemDto(
                mensagemSalva.getId(),
                mensagemSalva.getTexto(),
                mensagemSalva.getEmissor().getId(),
                mensagemSalva.getData()
        );
    }

    @Transactional
    public List<MensagemDto> listarMensagensDoEstudante(Usuario estudanteToken, long chatId) {
        Long estudanteId = estudanteToken.getId();

        Usuario estudante = usuarioRepository.findById(estudanteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante com ID " + estudanteId + " não encontrado."));

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat com ID " + chatId + " não encontrado."));

        boolean isChatParticipant = chat.getUsuario1().getId() == estudanteId ||
                chat.getPsicologo().getId() == estudanteId;

        if (!isChatParticipant) {
            throw new SecurityException("Acesso negado. O estudante com ID " + estudanteId + " não pertence ao chat com ID " + chatId + ".");
        }

        ChatParticipante participanteEstudante = chatParticipanteRepository.findByChatIdAndUsuarioId(chat.getId(), estudante.getId())
                .orElseThrow(() -> new IllegalStateException("Participante do estudante não encontrado no chat."));

        participanteEstudante.setMensagensNaoLidas(0);
        chatParticipanteRepository.save(participanteEstudante);

        var mensagens = mensagemRepository.findByChatIdAndRemovidoIsFalseOrderByDataDesc(chat.getId());

        return mensagens.stream().map(mensagem -> new MensagemDto(
                mensagem.getId(),
                mensagem.getTexto(),
                mensagem.getEmissor().getId(),
                mensagem.getData()
        )).toList();
    }

    public List<MensagemDto> listarMensagensDoUsuarioDeApoio(Usuario usuarioApoioToken, long chatId) {
        Long usuarioApoioId = usuarioApoioToken.getId();

        Usuario usuarioApoio = usuarioRepository.findById(usuarioApoioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário de Apoio com ID " + usuarioApoioId + " não encontrado."));

        boolean isUsuarioDeApoio = usuarioApoio.getRoles().stream()
                .anyMatch(role -> Objects.equals(role.getName(), Role.Values.PSICOLOGO.name()) ||
                        Objects.equals(role.getName(), Role.Values.SSR.name()));

        if (!isUsuarioDeApoio) {
            throw new SecurityException("Acesso negado. Usuário com ID " + usuarioApoioId + " não possui o perfil de PSICÓLOGO ou SSR.");
        }

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat com ID " + chatId + " não encontrado."));

        if (chat.getPsicologo().getId() != usuarioApoio.getId()) {
            throw new SecurityException("Você não tem permissão para listar mensagem neste chat.");
        }

        ChatParticipante participanteDestinatario = chatParticipanteRepository.findByChatIdAndUsuarioId(chat.getId(), chat.getPsicologo().getId())
                .orElseThrow(() -> new IllegalStateException("Participante destinatário não encontrado no chat."));

        participanteDestinatario.setMensagensNaoLidas(0);
        chatParticipanteRepository.save(participanteDestinatario);

        var mensagens = mensagemRepository.findByChatIdAndRemovidoIsFalseOrderByDataDesc(chat.getId());
        return mensagens.stream().map(mensagem -> new MensagemDto(
                mensagem.getId(),
                mensagem.getTexto(),
                mensagem.getEmissor().getId(),
                mensagem.getData()
        )).toList();
    }

    private void notificar(Usuario destinatario, String texto, long referenciaId) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(destinatario);
        notificacao.setTexto(texto);
        notificacao.setTipo(TipoNotificacao.CHAT);
        notificacao.setReferenciaId(referenciaId);
        notificacaoRepository.save(notificacao);
    }
}