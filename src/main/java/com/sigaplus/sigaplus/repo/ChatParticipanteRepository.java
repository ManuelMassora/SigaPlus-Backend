package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Chat;
import com.sigaplus.sigaplus.model.ChatParticipante;
import com.sigaplus.sigaplus.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatParticipanteRepository extends JpaRepository<ChatParticipante, Long> {
    Optional<ChatParticipante> findByChatIdAndUsuarioId(Long chatId, Long usuarioId);
}