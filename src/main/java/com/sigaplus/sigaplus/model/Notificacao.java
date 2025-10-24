package com.sigaplus.sigaplus.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notificacao")
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @JoinColumn(nullable = false)
    private String texto;
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private boolean lida = false;
    @Enumerated(EnumType.STRING)
    private TipoNotificacao tipo;
    private long referenciaId;

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(long referenciaId) {
        this.referenciaId = referenciaId;
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

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }
}
