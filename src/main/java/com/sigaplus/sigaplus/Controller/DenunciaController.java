package com.sigaplus.sigaplus.Controller;

import com.sigaplus.sigaplus.dto.CriarDenuncia;
import com.sigaplus.sigaplus.dto.DenunciaDto;
import com.sigaplus.sigaplus.service.DenunciaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("denuncia")
public class DenunciaController {
    private final DenunciaService denunciaService;

    public DenunciaController(DenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }

    @PostMapping("/{comentarioId}")
    public ResponseEntity<Void> denunciar(@PathVariable long comentarioId, @RequestBody CriarDenuncia dto) {
        denunciaService.denunciar(comentarioId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<DenunciaDto> buscar(@PathVariable long id) {
        return ResponseEntity.ok(denunciaService.buscar(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<DenunciaDto>> listar(Pageable pageable) {
        return ResponseEntity.ok(denunciaService.listar(pageable));
    }
}
