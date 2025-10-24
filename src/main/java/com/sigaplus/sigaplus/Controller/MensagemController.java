package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.EnviarMensagemDto;
import com.sigaplus.sigaplus.dto.MensagemDto;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.service.MensagemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensagem")
public class MensagemController {
    private final MensagemService mensagemService;

    public MensagemController(MensagemService mensagemService) {
        this.mensagemService = mensagemService;
    }

    @PostMapping("psicologia")
    @PreAuthorize("hasAnyAuthority('ESTUDANTE')")
    public ResponseEntity<MensagemDto> enviarPsicologia(@AuthenticationPrincipal Usuario usuario, @RequestBody EnviarMensagemDto dto) {
        var mensagem = mensagemService.enviarMensagem(usuario, dto, "PSICOLOGO");
        return ResponseEntity.ok(mensagem);
    }

    @PostMapping("ssr")
    @PreAuthorize("hasAnyAuthority('ESTUDANTE')")
    public ResponseEntity<MensagemDto> enviarSSR(@AuthenticationPrincipal Usuario usuario, @RequestBody EnviarMensagemDto dto) {
        var mensagem = mensagemService.enviarMensagem(usuario, dto, "SSR");
        return ResponseEntity.ok(mensagem);
    }

    @PostMapping("{id}")
    @PreAuthorize("hasAnyAuthority('PSICOLOGO','SSR')")
    public ResponseEntity<MensagemDto> enviarApoio(@AuthenticationPrincipal Usuario usuario, @PathVariable("id") long chatId, @RequestBody EnviarMensagemDto dto) {
        var mensagem = mensagemService.enviarMensagemPsicologo(usuario, dto, chatId);
        return ResponseEntity.ok(mensagem);
    }

    @GetMapping("chat/{id}")
    @PreAuthorize("hasAnyAuthority('ESTUDANTE')")
    public ResponseEntity<List<MensagemDto>> listarMensagem(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable("id") long chaId) {
        return ResponseEntity.ok(mensagemService.listarMensagensDoEstudante(usuario, chaId));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('PSICOLOGO','SSR')")
    public ResponseEntity<List<MensagemDto>> listarMensagensDoUsuarioDeApoio(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable("id") long chatId) {
        return ResponseEntity.ok(mensagemService.listarMensagensDoUsuarioDeApoio(usuario, chatId));
    }
}