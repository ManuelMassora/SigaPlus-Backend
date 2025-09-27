package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.ChatDto;
import com.sigaplus.sigaplus.model.Chat;
import com.sigaplus.sigaplus.model.Role;
import com.sigaplus.sigaplus.repo.ChatRepository;
import com.sigaplus.sigaplus.repo.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UsuarioRepository usuarioRepository;

    public ChatService(ChatRepository chatRepository, UsuarioRepository usuarioRepository) {
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
    }
    @Transactional
    public void criarChat(long estudanteId, long psicologoId) {
        var estudante = usuarioRepository.findById(estudanteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante com ID " + estudanteId + " nao encontrado."));
        var psicologo = usuarioRepository.findById(psicologoId)
                .orElseThrow(() -> new EntityNotFoundException("Psicologo com ID " + psicologoId + " nao encontrado."));
        boolean isPsicologo = psicologo.getRoles().stream()
                .anyMatch(role -> Objects.equals(role.getName(), Role.Values.PSICOLOGO.name()));
        if (!isPsicologo) {
            throw new IllegalArgumentException("Usuario com ID " + psicologoId + " nao possui o perfil de PSICOLOGO.");
        }
        Chat chat = new Chat();
        chat.setUsuario1(estudante);
        chat.setPsicologo(psicologo);
        chatRepository.save(chat);
    }

    @Transactional
    public void apagarChat(JwtAuthenticationToken token, long chatId) {
        long usuarioId = Long.parseLong(token.getName());
        var chat = chatRepository.findByUsuario1Id(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario sem chat"));
        chat.setRemovido(true);
        chatRepository.save(chat);
    }

    public ChatDto listarChat(JwtAuthenticationToken token) {
        long usuarioId = Long.parseLong(token.getName());
        var chat = chatRepository.findByUsuario1Id(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario sem chat"));
        return new ChatDto(
                chat.getId(),
                chat.getCriadoEm()
        );
    }

    public Page<ChatDto> listarChatPsicologo(JwtAuthenticationToken token, Pageable pageable) {
        long usuarioId = Long.parseLong(token.getName());
        var chats = chatRepository.findAllByPsicologoId(usuarioId, pageable);
        return chats.map(chat -> new ChatDto(
                chat.getId(),
                chat.getCriadoEm()
        ));
    }
}
