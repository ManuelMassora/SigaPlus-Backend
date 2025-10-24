package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.CriarPostagem;
import com.sigaplus.sigaplus.dto.PostagemDto;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.service.PostagemService;
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
@RequestMapping("postagem")
public class PostagemController {
    private final PostagemService postagemService;

    public PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    @PostMapping("/{topicoId}")
    public ResponseEntity<Void> criar(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable long topicoId,
            @RequestBody @Valid CriarPostagem dto) {

        postagemService.criar(usuario, topicoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{postagemId}")
    public ResponseEntity<Void> remover(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable long postagemId) {

        postagemService.remover(usuario, postagemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{postagemId}")
    public ResponseEntity<Void> editar(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable long postagemId,
            @RequestBody @Valid CriarPostagem dto) {

        postagemService.editar(usuario, postagemId, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postagemId}")
    public ResponseEntity<PostagemDto> buscar(@PathVariable long postagemId) {
        PostagemDto postagemDto = postagemService.buscar(postagemId);
        return ResponseEntity.ok(postagemDto);
    }

    @GetMapping("/topico/{topicoId}")
    public ResponseEntity<Page<PostagemDto>> listarPorTopicoId(
            @PathVariable long topicoId,
            @PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable pageable) {

        Page<PostagemDto> postagens = postagemService.listarPorTopicoId(topicoId, pageable);
        return ResponseEntity.ok(postagens);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<PostagemDto>> listarMinhasPostagens(
            @AuthenticationPrincipal Usuario usuario,
            @PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable pageable) {

        Page<PostagemDto> postagens = postagemService.listarMinhasPostagens(usuario, pageable);
        return ResponseEntity.ok(postagens);
    }

    @PostMapping("/{postagemId}/curtir")
    public ResponseEntity<Void> curtir(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable long postagemId) {
        postagemService.curtir(usuario, postagemId);
        return ResponseEntity.ok().build();
    }
}
