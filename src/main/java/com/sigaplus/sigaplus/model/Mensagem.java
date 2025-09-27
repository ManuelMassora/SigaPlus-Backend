package com.sigaplus.sigaplus.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Mensagem")
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "Tipo", length = 500)
    private String tipo = "ANONIMO";
    @Column(name = "Texto", length = 500, columnDefinition = "TEXT")
    private String texto;
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
    @ManyToOne
    @JoinColumn(name = "emissor_id", nullable = false)
    private Usuario emissor;
    @Column(name = "Data", length = 500)
    private LocalDateTime data = LocalDateTime.now();
    private boolean anonimo = true;
    private boolean removido = false;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Usuario getEmissor() {
        return emissor;
    }

    public void setEmissor(Usuario emissor) {
        this.emissor = emissor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public boolean isAnonimo() {
        return anonimo;
    }

    public void setAnonimo(boolean anonimo) {
        this.anonimo = anonimo;
    }
}
