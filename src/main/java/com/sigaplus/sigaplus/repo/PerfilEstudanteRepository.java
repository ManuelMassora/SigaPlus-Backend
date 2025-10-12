package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.PerfilEstudante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilEstudanteRepository extends JpaRepository<PerfilEstudante, Long> {
    Optional<PerfilEstudante> findByUsuarioId(long usuarioId);
}