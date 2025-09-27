package com.sigaplus.sigaplus.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;
    @ManyToOne
    @JoinColumn(name="usuario1_id")
    private Usuario usuario1;
    @ManyToOne
    @JoinColumn(name="psicologo_id")
    private Usuario psicologo;
    private LocalDateTime criadoEm = LocalDateTime.now();
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Mensagem> mensagens;
    private boolean removido = false;

    public Usuario getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(Usuario usuario1) {
        this.usuario1 = usuario1;
    }

    public Usuario getPsicologo() {
        return psicologo;
    }

    public void setPsicologo(Usuario psicologo) {
        this.psicologo = psicologo;
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

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
}