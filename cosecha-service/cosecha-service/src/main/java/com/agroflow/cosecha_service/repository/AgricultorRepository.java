package com.agroflow.cosecha_service.repository;

import com.agroflow.cosecha_service.model.Agricultor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgricultorRepository extends JpaRepository<Agricultor, Long> {
}
