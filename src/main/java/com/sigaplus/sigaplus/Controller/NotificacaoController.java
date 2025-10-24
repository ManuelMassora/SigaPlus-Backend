package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.NotificacaoDto;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.service.NotificacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable long id){
        return ResponseEntity.ok(notificacaoService.buscar(usuario, id));
    }

    @GetMapping
    public ResponseEntity<Page<NotificacaoDto>> listar(@AuthenticationPrincipal Usuario usuario, Pageable pageable) {
        return ResponseEntity.ok(notificacaoService.listar(usuario, pageable));
    }

    @PostMapping("/marcartodas")
    public ResponseEntity<Integer> marcarTodasComoLidas(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(notificacaoService.marcarTodasComoLida(usuario));
    }
}
