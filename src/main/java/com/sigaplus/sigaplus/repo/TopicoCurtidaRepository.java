package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.TopicoCurtida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoCurtidaRepository extends JpaRepository<TopicoCurtida, Long> {
    boolean existsByTopicoIdAndUsuarioId(long topicoId, long usuarioId);
}