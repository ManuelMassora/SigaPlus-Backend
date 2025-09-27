package com.sigaplus.sigaplus.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record MensagemDto(long id, String texto, long emissorId, LocalDateTime data) {
}