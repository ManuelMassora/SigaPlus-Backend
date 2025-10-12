package com.sigaplus.sigaplus.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record TopicoDto(long id, String titulo, String descricao, int curtidas, int visualizacoes,
                        LocalDateTime dataCriacao, PerfilEstudanteDto criador) implements Serializable {
}