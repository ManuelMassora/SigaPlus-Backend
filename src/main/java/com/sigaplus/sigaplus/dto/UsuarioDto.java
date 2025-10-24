package com.sigaplus.sigaplus.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.sigaplus.sigaplus.model.Usuario}
 */
public record UsuarioDto(long id, String nome, String email, boolean ativo) implements Serializable {
}