package com.agroflow3.facturacion_service.controller;

import com.agroflow3.facturacion_service.model.Factura;
import com.agroflow3.facturacion_service.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService service;

    @GetMapping
    public List<Factura> listarTodas() {
        return service.listarTodas();
    }
}
