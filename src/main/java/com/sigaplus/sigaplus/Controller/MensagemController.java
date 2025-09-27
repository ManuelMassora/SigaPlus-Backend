package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.EnviarMensagemDto;
import com.sigaplus.sigaplus.dto.MensagemDto;
import com.sigaplus.sigaplus.service.MensagemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ESTUDANTE')")
    public ResponseEntity<MensagemDto> enviar(JwtAuthenticationToken token, @RequestBody EnviarMensagemDto dto) {
        var mensagem = mensagemService.enviarMensagem(token, dto);
        return ResponseEntity.ok(mensagem);
    }

    @PostMapping("{id}")
    @PreAuthorize("hasAnyAuthority('PSICOLOGO')")
    public ResponseEntity<MensagemDto> enviarPsicologo(JwtAuthenticationToken token, @PathVariable("id") long chatId, @RequestBody EnviarMensagemDto dto) {
        var mensagem = mensagemService.enviarMensagemPsicologo(token, dto, chatId);
        return ResponseEntity.ok(mensagem);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ESTUDANTE')")
    public ResponseEntity<List<MensagemDto>> listarMensagem(JwtAuthenticationToken token) {
        return ResponseEntity.ok(mensagemService.listarMensagensDoEstudante(token));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('PSICOLOGO')")
    public ResponseEntity<List<MensagemDto>> listarMensagemPsicologo(
            JwtAuthenticationToken token,
            @PathVariable("id") long chatId) {
        return ResponseEntity.ok(mensagemService.listarMensagensDoPsicologo(token, chatId));
    }
}