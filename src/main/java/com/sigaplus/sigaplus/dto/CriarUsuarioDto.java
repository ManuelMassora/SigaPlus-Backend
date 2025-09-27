package com.sigaplus.sigaplus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link com.sigaplus.sigaplus.model.Usuario}
 */
public record CriarUsuarioDto(@NotBlank String nome, @Email @NotBlank String email,
                              @NotBlank String senha, String role) implements Serializable {
}