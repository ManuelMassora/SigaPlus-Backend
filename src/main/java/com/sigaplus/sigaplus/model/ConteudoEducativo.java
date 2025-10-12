package com.sigaplus.sigaplus.model;

import jakarta.persistence.*;

@Entity
@Table(name = "conteudo_educativo")
public class ConteudoEducativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String titulo;
    private String descricao;
    private String urlImagem;
    private Byte conteudo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public Byte getConteudo() {
        return conteudo;
    }

    public void setConteudo(Byte conteudo) {
        this.conteudo = conteudo;
    }
}