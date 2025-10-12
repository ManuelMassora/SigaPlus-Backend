package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.NotificacaoDto;
import com.sigaplus.sigaplus.service.NotificacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notificacao")
public class NotificacaoController {
    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacaoDto> buscar(
            JwtAuthenticationToken token,
            @PathVariable long id){
        return ResponseEntity.ok(notificacaoService.buscar(token, id));
    }

    @GetMapping
    public ResponseEntity<Page<NotificacaoDto>> listar(JwtAuthenticationToken token, Pageable pageable) {
        return ResponseEntity.ok(notificacaoService.listar(token, pageable));
    }

    @PostMapping("/marcartodas")
    public ResponseEntity<Integer> marcarTodasComoLidas(JwtAuthenticationToken token) {
        return ResponseEntity.ok(notificacaoService.marcarTodasComoLida(token));
    }
}
