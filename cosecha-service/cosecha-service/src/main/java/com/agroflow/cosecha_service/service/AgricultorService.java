package com.agroflow.cosecha_service.service;

import com.agroflow.cosecha_service.dto.AgricultorDTO;
import com.agroflow.cosecha_service.model.Agricultor;
import com.agroflow.cosecha_service.repository.AgricultorRepository;
import org.springframework.stereotype.Service;

@Service
public class AgricultorService {
    private final AgricultorRepository agricultorRepository;

    public AgricultorService(AgricultorRepository agricultorRepository) {
        this.agricultorRepository = agricultorRepository;
    }

    public Agricultor registrarAgricultor(AgricultorDTO agricultorDTO) {
        Agricultor agricultor = new Agricultor();
        agricultor.setNombre(agricultorDTO.getNombre());
        agricultor.setApellido(agricultorDTO.getApellido());

        return agricultorRepository.save(agricultor);
    }

    public Agricultor obtenerAgricultor(Long id) {
        return agricultorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agricultor no encontrado"));
    }

}
