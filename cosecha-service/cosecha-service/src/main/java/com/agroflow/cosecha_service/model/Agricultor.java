package com.agroflow.cosecha_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;

@Entity
@Table(name = "agricultores")
@Getter
@Setter
public class Agricultor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 50)
    private String nombre;

    @Column(name = "lastname", nullable = false)
    private String apellido;


}
