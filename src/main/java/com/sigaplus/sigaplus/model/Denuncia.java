package com.sigaplus.sigaplus.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "denuncia")
public class Denuncia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Comentario comentario;
    @Column(nullable = false)
    private String motivo;
    private LocalDateTime dataCriacao = LocalDateTime.now();
}
