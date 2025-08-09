package com.agroflow.cosecha_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CosechaDTO {
    private Long id;
    private String agricultorId;
    private String producto;

    @JsonProperty("tonelada")
    private Double tonelada;

    private String ubicacion;
    private LocalDateTime fechaRegistro;
    private String estado;


    public String getId() {
        return id != null ? id.toString() : null;
    }

    // Setter para mantener compatibilidad
    public void setId(Long id) {
        this.id = id;
    }


    public void setId(String id) {
        this.id = id != null ? Long.parseLong(id) : null;
    }
}