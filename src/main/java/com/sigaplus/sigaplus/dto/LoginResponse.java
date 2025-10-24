package com.sigaplus.sigaplus.dto;

import java.util.List;

public record LoginResponse(List<String> userRoles, Long expiresIn) {
}