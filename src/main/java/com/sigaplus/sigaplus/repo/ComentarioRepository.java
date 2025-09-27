package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}