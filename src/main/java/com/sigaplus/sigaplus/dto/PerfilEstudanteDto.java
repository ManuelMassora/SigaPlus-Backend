package com.sigaplus.sigaplus.dto;

import java.io.Serializable;

public record PerfilEstudanteDto(String nome, String curso, String anoAcademico, String sobreMim,
                                 String EspecialidadeEm, String urlImagem) implements Serializable {
}