package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.CriarTopico;
import com.sigaplus.sigaplus.dto.TopicoDto;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.service.TopicoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("topico")
public class TopicoController {
    private final TopicoService topicoService;

    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarTopico(
            @AuthenticationPrincipal Usuario usuario,
            @RequestBody @Valid CriarTopico dto) {

        topicoService.criarTopico(usuario, dto);
    }

    @DeleteMapping("/{topicoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerTopico(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable long topicoId) {
        topicoService.removerTopico(usuario, topicoId);
    }

    @GetMapping("/{topicoId}")
    public ResponseEntity<TopicoDto> buscarTopico(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable long topicoId) {

        try {
            TopicoDto topico = topicoService.buscarTopico(usuario, topicoId);
            return ResponseEntity.ok(topico);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<TopicoDto>> listarTopicos(
            @RequestParam(required = false) String titulo,
            @PageableDefault(size = 10, sort = "dataCriacao") Pageable pageable) {

        Page<TopicoDto> topicos = topicoService.listarTopicos(titulo, pageable);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("my")
    public ResponseEntity<Page<TopicoDto>> listarMeusTopicos(
            @AuthenticationPrincipal Usuario usuario,
            @PageableDefault(size = 10, sort = "dataCriacao") Pageable pageable) {

        Page<TopicoDto> topicos = topicoService.listarMeusTopicos(usuario, pageable);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("curtidos")
    public ResponseEntity<Page<TopicoDto>> listarTopicosCurtidos(
            @AuthenticationPrincipal Usuario usuario,
            @PageableDefault(size = 10, sort = "dataCriacao") Pageable pageable) {

        Page<TopicoDto> topicos = topicoService.listarTopicosCurtidos(usuario, pageable);
        return ResponseEntity.ok(topicos);
    }

    @PostMapping("/{topicoId}/curtir")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void curtirTopico(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable long topicoId) {

        topicoService.curtirTopico(usuario, topicoId);
    }

}