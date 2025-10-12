package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Postagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {
    Optional<Postagem> findByIdAndRemovidoIsFalse(long id);
    Optional<Postagem> findByIdAndUsuarioIdAndRemovidoIsFalse(long postagemId, long usuarioId);
    Page<Postagem> findAllByTopicoIdAndRemovidoIsFalse(long topicoId, Pageable pageable);
}