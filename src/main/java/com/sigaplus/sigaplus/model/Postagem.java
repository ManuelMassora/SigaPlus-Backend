package com.sigaplus.sigaplus.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "postagem")
public class Postagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Topico topico;
    @Column(length = 1000, nullable = false)
    private String conteudo;
    @OneToMany(mappedBy = "postagem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostagemCurtida> curtidas = new HashSet<>();

    public int getCurtidas() {
        return curtidas.size();
    }

    private LocalDateTime dataCriacao = LocalDateTime.now();
    private boolean removido = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Topico getTopico() {
        return topico;
    }

    public void setTopico(Topico topico) {
        this.topico = topico;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isRemovido() {
        return removido;
    }

    public void setRemovido(boolean removido) {
        this.removido = removido;
    }
}
