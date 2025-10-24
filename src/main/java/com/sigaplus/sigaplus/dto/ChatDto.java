package com.sigaplus.sigaplus.dto;

import java.time.LocalDateTime;

public record ChatDto(long id, String tipo, LocalDateTime criadoEm) {
}