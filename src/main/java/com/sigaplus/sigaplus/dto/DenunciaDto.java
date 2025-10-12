package com.sigaplus.sigaplus.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.sigaplus.sigaplus.model.Denuncia}
 */
public record DenunciaDto(long id, String motivo, LocalDateTime dataCriacao, ComentarioDto comentario) implements Serializable {
}