package com.agroflow.cosecha_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EstadoDTO {
    private String estado;
    private String facturaId;
}
