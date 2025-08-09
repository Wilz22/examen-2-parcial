package com.agroflow.cosecha_service.controller;

import com.agroflow.cosecha_service.dto.CosechaDTO;
import com.agroflow.cosecha_service.dto.EstadoDTO;
import com.agroflow.cosecha_service.model.Cosecha;
import com.agroflow.cosecha_service.service.CosechaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cosechas")
public class CosechaController {
    private final CosechaService cosechaService;

    public CosechaController(CosechaService cosechaService) {
        this.cosechaService = cosechaService;
    }

    @PostMapping
    public ResponseEntity<Cosecha> registrarCosecha(@RequestBody CosechaDTO cosechaDTO) {
        Cosecha cosecha = cosechaService.registrarCosecha(cosechaDTO);
        return ResponseEntity.ok(cosecha);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable Long id,
            @RequestBody EstadoDTO estadoDTO) {
        cosechaService.actualizarEstado(id, estadoDTO.getEstado(), estadoDTO.getFacturaId());
        return ResponseEntity.ok().build();
    }
}