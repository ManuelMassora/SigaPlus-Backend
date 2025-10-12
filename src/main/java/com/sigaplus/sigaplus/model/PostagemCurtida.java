package com.sigaplus.sigaplus.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "postagem_curtida", uniqueConstraints = {
        // Garante que um usuário só pode curtir um tópico UMA VEZ.
        @UniqueConstraint(columnNames = {"postagem_id", "usuario_id"})
})
public class PostagemCurtida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postagem_id", nullable = false)
    private Postagem postagem;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Postagem getPostagem() {
        return postagem;
    }

    public void setPostagem(Postagem postagem) {
        this.postagem = postagem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
