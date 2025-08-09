package com.agroflow.cosecha_service.controller;

import com.agroflow.cosecha_service.dto.AgricultorDTO;
import com.agroflow.cosecha_service.model.Agricultor;
import com.agroflow.cosecha_service.service.AgricultorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agricultores")
public class AgricultorController {
    private final AgricultorService agricultorService;

    public AgricultorController(AgricultorService agricultorService) {
        this.agricultorService = agricultorService;
    }

    @PostMapping
    public ResponseEntity<Agricultor> registrarAgricultor(@RequestBody AgricultorDTO agricultorDTO) {
        Agricultor agricultor = agricultorService.registrarAgricultor(agricultorDTO);
        return ResponseEntity.ok(agricultor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agricultor> obtenerAgricultor(@PathVariable Long id) {
        Agricultor agricultor = agricultorService.obtenerAgricultor(id);
        return ResponseEntity.ok(agricultor);
    }


}
