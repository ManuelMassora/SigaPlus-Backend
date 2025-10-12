package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.CriarPostagem;
import com.sigaplus.sigaplus.dto.PostagemDto;
import com.sigaplus.sigaplus.service.PostagemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            JwtAuthenticationToken token,
            @PathVariable long topicoId,
            @RequestBody @Valid CriarPostagem dto) {

        postagemService.criar(token, topicoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{postagemId}")
    public ResponseEntity<Void> remover(
            JwtAuthenticationToken token,
            @PathVariable long postagemId) {

        postagemService.remover(token, postagemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{postagemId}")
    public ResponseEntity<Void> editar(
            JwtAuthenticationToken token,
            @PathVariable long postagemId,
            @RequestBody @Valid CriarPostagem dto) {

        postagemService.editar(token, postagemId, dto);
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
}
