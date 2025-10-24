package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Optional<Topico> findByIdAndRemovidoIsFalse(long id);
    Optional<Topico> findFirstByIdAndUsuarioIdAndRemovidoIsFalse(long id, long usuarioId);
    Page<Topico> findAllByTituloContainingAndRemovidoIsFalse(String titulo, Pageable pageable);
    Page<Topico> findByUsuarioIdAndRemovidoFalse(long usuarioId, Pageable pageable);

    @Query("SELECT distinct t FROM Topico t JOIN  t.curtidas tc WHERE t.removido=false AND tc.usuario.id = :usuarioId")
    Page<Topico> findTopicosCurtidosPorUsuario(@Param("usuarioId") long usuarioId, Pageable pageable);
}