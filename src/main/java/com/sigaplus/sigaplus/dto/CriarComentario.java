package com.sigaplus.sigaplus.dto;

import java.io.Serializable;

public record CriarComentario(String conteudo, Long comentarioId) implements Serializable {
}