package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findByChatIdAndRemovidoIsFalseOrderByDataDesc(long chatId);
}