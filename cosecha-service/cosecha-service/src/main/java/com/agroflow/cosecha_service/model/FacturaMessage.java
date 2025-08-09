package com.agroflow.cosecha_service.model;

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