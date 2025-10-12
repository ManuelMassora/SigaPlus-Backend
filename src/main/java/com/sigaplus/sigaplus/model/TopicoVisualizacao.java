package com.sigaplus.sigaplus.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "topico_visualizacao", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"topico_id", "usuario_id"})
})
public class TopicoVisualizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id", nullable = false)
    private Topico topico;

    private LocalDateTime dataVisualizacao = LocalDateTime.now();

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

    public Topico getTopico() {
        return topico;
    }

    public void setTopico(Topico topico) {
        this.topico = topico;
    }

    public LocalDateTime getDataVisualizacao() {
        return dataVisualizacao;
    }

    public void setDataVisualizacao(LocalDateTime dataVisualizacao) {
        this.dataVisualizacao = dataVisualizacao;
    }
}