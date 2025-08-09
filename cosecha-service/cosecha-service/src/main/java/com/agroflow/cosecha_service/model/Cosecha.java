package com.agroflow.cosecha_service.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;


@Entity
@Table(name = "cosechas")
@Getter
@Setter
public class Cosecha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String producto;

    @Column(nullable = false)
    private Double toneladas;


    @Column(nullable = false)
    private String ubicacion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_agricultor")
    private Agricultor agricultor;

}
