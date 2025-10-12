package com.sigaplus.sigaplus.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record CriarPostagem(@NotBlank String conteudo) implements Serializable {
}