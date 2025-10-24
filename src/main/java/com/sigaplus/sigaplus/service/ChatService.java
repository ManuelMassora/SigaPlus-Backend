package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.ChatDto;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.ChatRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<ChatDto> listarChat(Usuario usuario) {
        var chats = chatRepository.findAllByUsuario1Id(usuario.getId());
        if (chats.isEmpty()) {
            new EntityNotFoundException("Usuario sem chat");
        }
        return chats.stream().map(chat -> new ChatDto(
                chat.getId(),
                chat.getTipoChat().name(),
                chat.getCriadoEm()
        )).toList();
    }

    public Page<ChatDto> listarChatPsicologo(Usuario usuario, Pageable pageable) {
        var chats = chatRepository.findAllByPsicologoId(usuario.getId(), pageable);
        return chats.map(chat -> new ChatDto(
                chat.getId(),
                chat.getTipoChat().name(),
                chat.getCriadoEm()
        ));
    }
}