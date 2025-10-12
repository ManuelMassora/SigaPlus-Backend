package com.sigaplus.sigaplus.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topico")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @Column(length = 255, nullable = false)
    private String titulo;
    @Column(length = 500, nullable = true)
    private String descricao;
    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TopicoCurtida> curtidas = new HashSet<>();

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TopicoVisualizacao> visualizacoes = new HashSet<>();
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private boolean removido = false;

    public int getTotalCurtidas() {
        return this.curtidas.size();
    }

    public int getTotalVisualizacoes() {
        return this.visualizacoes.size();
    }



    public boolean isRemovido() {
        return removido;
    }

    public void setRemovido(boolean removido) {
        this.removido = removido;
    }

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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
