package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.TopicoVisualizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoVisualizacaoRepository extends JpaRepository<TopicoVisualizacao, Long> {
    boolean existsByTopicoIdAndUsuarioId(long topicoId, long usuario);
}