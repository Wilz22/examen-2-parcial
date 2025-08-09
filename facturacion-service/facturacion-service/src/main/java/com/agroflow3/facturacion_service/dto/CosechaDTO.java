package com.agroflow3.facturacion_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CosechaDTO {
    // Para recibir mensajes del microservicio de cosecha
    @JsonProperty("id")
    private Long id;

    private String producto;

    @JsonProperty("tonelada")
    private Double tonelada;

    // Para uso interno en facturación (campos adicionales)
    private String cosechaId;
    private Double cantidad;

    // Métodos helper para obtener el ID como string
    public String getIdAsString() {
        return id != null ? id.toString() : cosechaId;
    }


    public Double getCantidadTotal() {
        return tonelada != null ? tonelada : cantidad;
    }
}