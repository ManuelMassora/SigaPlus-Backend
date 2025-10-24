package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.ComentarioDto;
import com.sigaplus.sigaplus.dto.CriarComentario;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.service.ComentarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comentario")
public class ComentarioController {
    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping("/{postagemId}")
    public ResponseEntity<Void> criar(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable long postagemId,
            @RequestBody CriarComentario dto) {
        comentarioService.criar(usuario, postagemId, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{comentarioId}")
    public ResponseEntity<Void> editar(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable long comentarioId,
            @RequestBody CriarComentario dto) {
        comentarioService.editar(usuario, comentarioId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{comentarioId}")
    public ResponseEntity<Void> remover(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable long comentarioId) {
        comentarioService.remover(usuario, comentarioId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDto> buscar(@PathVariable long id) {
        return ResponseEntity.ok(comentarioService.buscar(id));
    }

    @GetMapping("/postagem/{postagemId}")
    public ResponseEntity<Page<ComentarioDto>> listarPorPostagem(
            @PathVariable long postagemId,
            Pageable pageable) {
        return ResponseEntity.ok(comentarioService.listarPorPostagemId(postagemId, pageable));
    }
}
