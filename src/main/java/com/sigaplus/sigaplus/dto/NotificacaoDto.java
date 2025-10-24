package com.sigaplus.sigaplus.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.sigaplus.sigaplus.model.Notificacao}
 */
public record NotificacaoDto(long id, String texto,
                             LocalDateTime dataCriacao,
                             boolean lida, String tipo,
                             long referenciaId) implements Serializable {
}