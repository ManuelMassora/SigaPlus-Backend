package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.ChatDto;
import com.sigaplus.sigaplus.model.Chat;
import com.sigaplus.sigaplus.service.ChatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ESTUDANTE')")
    public ResponseEntity<ChatDto> chatPrivado(JwtAuthenticationToken token) {
        return ResponseEntity.ok(chatService.listarChat(token));
    }

    @GetMapping("/psicologo")
    @PreAuthorize("hasAnyAuthority('PSICOLOGO')")
    public ResponseEntity<Page<ChatDto>> chatsPrivado(JwtAuthenticationToken token,
                                                   @PageableDefault(size = 10, sort = {"criadoEm"})Pageable pageable) {
        return ResponseEntity.ok(chatService.listarChatPsicologo(token, pageable));
    }
}