package com.agroflow2.inventario_service.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CosechaDTO {
    private String id;
    private String producto;
    private Double tonelada;
}