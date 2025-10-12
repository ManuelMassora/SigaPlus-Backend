package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Optional<Topico> findByIdAndRemovidoIsFalse(long id);
    Optional<Topico> findFirstByIdAndUsuarioIdAndRemovidoIsFalse(long id, long usuarioId);
    Page<Topico> findAllByTituloContainingAndRemovidoIsFalse(String titulo, Pageable pageable);
}