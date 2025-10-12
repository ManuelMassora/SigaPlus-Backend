package com.sigaplus.sigaplus.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record CriarTopico(@NotBlank String titulo, @NotBlank String descricao) implements Serializable {
}