package com.sigaplus.sigaplus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
public class ChatParticipante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    private Usuario usuario;

    private int mensagensNaoLidas = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getMensagensNaoLidas() {
        return mensagensNaoLidas;
    }

    public void setMensagensNaoLidas(int mensagensNaoLidas) {
        this.mensagensNaoLidas = mensagensNaoLidas;
    }
}
