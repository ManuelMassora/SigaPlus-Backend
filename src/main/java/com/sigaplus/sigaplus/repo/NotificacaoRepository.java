package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Notificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    Optional<Notificacao> findByIdAndUsuarioId(long id, long usuarioId);
    Page<Notificacao> findAllByUsuarioId(long usuarioId, Pageable pageable);
    @Modifying
    @Query("UPDATE Notificacao n SET n.lida = true WHERE n.usuario.id = :usuarioId AND n.lida = false")
    int marcarTodasComoLidas(@Param("usuarioId") long usuarioID);
}