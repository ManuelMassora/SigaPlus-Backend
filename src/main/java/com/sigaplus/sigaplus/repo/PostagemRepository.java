package com.sigaplus.sigaplus.repo;

import com.sigaplus.sigaplus.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {
}