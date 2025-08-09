package com.agroflow.cosecha_service.repository;

import com.agroflow.cosecha_service.model.Agricultor;
import com.agroflow.cosecha_service.model.Cosecha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CosechaRepository extends JpaRepository<Cosecha, Long> {
}
