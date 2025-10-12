package com.sigaplus.sigaplus.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ComentarioDto(long id, String conteudo,
                            LocalDateTime dataCriacao,
                            String usuarioRespondido,
                            PerfilEstudanteDto usuario) implements Serializable {
}