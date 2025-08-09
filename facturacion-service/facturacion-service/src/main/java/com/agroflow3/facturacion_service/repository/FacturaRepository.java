package com.agroflow3.facturacion_service.repository;

import com.agroflow3.facturacion_service.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, String> {
}
