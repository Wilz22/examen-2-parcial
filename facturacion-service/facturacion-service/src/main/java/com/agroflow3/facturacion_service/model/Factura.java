package com.agroflow3.facturacion_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String facturaId;

    @Column(name = "cosecha_id", nullable = false)
    private String cosechaId;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private Boolean pagada = false;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}
