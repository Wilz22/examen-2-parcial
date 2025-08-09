package com.agroflow2.inventario_service.repository;


import com.agroflow2.inventario_service.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {
    Insumo findByNombreInsumo(String nombreInsumo);
}