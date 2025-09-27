package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUsuario1Id(long usuarioId);
    Page<Chat> findAllByPsicologoId(long usuario2Id, Pageable pageable);
}