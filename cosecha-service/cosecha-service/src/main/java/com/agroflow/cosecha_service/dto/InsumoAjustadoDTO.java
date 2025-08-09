package com.agroflow.cosecha_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsumoAjustadoDTO {
    private String cosechaId;
    private String insumo;
    private double cantidadDeducida;
    private double nuevoStock;
}