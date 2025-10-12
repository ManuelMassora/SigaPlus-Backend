package com.sigaplus.sigaplus.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record PostagemDto(long id, String conteudo, LocalDateTime dataCriacao, int curtidas,
                          long topicoId, PerfilEstudanteDto usuario) implements Serializable {
}