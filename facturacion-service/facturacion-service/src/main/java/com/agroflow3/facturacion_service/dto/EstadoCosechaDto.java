package com.agroflow3.facturacion_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoCosechaDto {
    private String estado;
    private String facturaId;
}
