package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Comentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    Optional<Comentario> findByIdAndRemovidoIsFalse(Long id);
    Optional<Comentario> findByIdAndUsuarioIdAndRemovidoIsFalse(long id, long usuarioId);
    Page<Comentario> findAllByPostagemIdAndRemovidoIsFalseOrderByDataCriacaoDesc(long postagemId,Pageable pageable);
}