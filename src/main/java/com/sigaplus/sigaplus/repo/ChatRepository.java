package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Chat;
import com.sigaplus.sigaplus.model.TipoChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByUsuario1Id(long usuarioId);
    Optional<Chat> findByUsuario1Id(long usuarioId);
    Page<Chat> findAllByPsicologoId(long usuario2Id, Pageable pageable);
    Optional<Chat> findByUsuario1IdAndTipoChat(Long usuario1Id, TipoChat tipoChat);
}