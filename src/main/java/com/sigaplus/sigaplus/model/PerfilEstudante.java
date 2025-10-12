package com.sigaplus.sigaplus.model;

import jakarta.persistence.*;

@Entity
@Table(name = "perfil_estudante")
public class PerfilEstudante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;
    private String nome;
    private String curso;
    private String anoAcademico;
    private String sobreMim;
    private String EspecialidadeEm;
    private String urlImagem;

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getAnoAcademico() {
        return anoAcademico;
    }

    public void setAnoAcademico(String anoAcademico) {
        this.anoAcademico = anoAcademico;
    }

    public String getSobreMim() {
        return sobreMim;
    }

    public void setSobreMim(String sobreMim) {
        this.sobreMim = sobreMim;
    }

    public String getEspecialidadeEm() {
        return EspecialidadeEm;
    }

    public void setEspecialidadeEm(String especialidadeEm) {
        EspecialidadeEm = especialidadeEm;
    }
}