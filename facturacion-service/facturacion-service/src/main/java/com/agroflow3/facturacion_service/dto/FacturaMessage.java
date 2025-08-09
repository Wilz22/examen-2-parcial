package com.agroflow3.facturacion_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaMessage {
    private String cosechaId;
    private String facturaId;
}