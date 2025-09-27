package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.CriarEstudanteDto;
import com.sigaplus.sigaplus.dto.CriarUsuarioDto;
import com.sigaplus.sigaplus.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("cadastro")
    public ResponseEntity<Void> criarEstudante(@RequestBody CriarEstudanteDto dto) {
        usuarioService.CriarEstudante(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("admin/cadastro")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> criarContaInterna(@RequestBody CriarUsuarioDto dto) {
        usuarioService.CriarUsuarioInterno(dto);
        return ResponseEntity.ok().build();
    }
}
