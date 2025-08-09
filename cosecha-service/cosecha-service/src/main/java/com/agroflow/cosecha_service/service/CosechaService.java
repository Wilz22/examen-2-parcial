package com.agroflow.cosecha_service.service;

import com.agroflow.cosecha_service.dto.CosechaDTO;
import com.agroflow.cosecha_service.model.Agricultor;
import com.agroflow.cosecha_service.model.Cosecha;
import com.agroflow.cosecha_service.repository.CosechaRepository;
import com.agroflow.cosecha_service.repository.AgricultorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class CosechaService {

    private static final Logger logger = LoggerFactory.getLogger(CosechaService.class);

    private final CosechaRepository cosechaRepository;
    private final AgricultorRepository agricultorRepository;
    private final RabbitTemplate rabbitTemplate;

    public CosechaService(CosechaRepository cosechaRepository,
                          AgricultorRepository agricultorRepository,
                          RabbitTemplate rabbitTemplate) {
        this.cosechaRepository = cosechaRepository;
        this.agricultorRepository = agricultorRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Cosecha registrarCosecha(CosechaDTO cosechaDTO) {
        logger.info("Registrando nueva cosecha: {}", cosechaDTO);

        Long agricultorId = Long.parseLong(cosechaDTO.getAgricultorId());

        Agricultor agricultor = agricultorRepository.findById(agricultorId)
                .orElseThrow(() -> new RuntimeException("Agricultor no encontrado"));

        Cosecha cosecha = new Cosecha();
        cosecha.setProducto(cosechaDTO.getProducto());
        cosecha.setToneladas(cosechaDTO.getTonelada());
        cosecha.setUbicacion(cosechaDTO.getUbicacion());
        cosecha.setEstado("REGISTRADA");
        cosecha.setFechaRegistro(LocalDateTime.now());
        cosecha.setAgricultor(agricultor);

        Cosecha savedCosecha = cosechaRepository.saveAndFlush(cosecha);

        // Verificar que el ID fue generado correctamente
        if (savedCosecha.getId() == null) {
            logger.error("Error: La cosecha guardada no tiene ID generado");
            throw new RuntimeException("Error al guardar cosecha: ID no generado");
        }

        logger.info("Cosecha guardada: id={}, producto={}, estado={}",
                savedCosecha.getId(), savedCosecha.getProducto(), savedCosecha.getEstado());

        // Crear un DTO para enviar a la cola con los datos necesarios
        CosechaDTO messageDTO = new CosechaDTO();
        messageDTO.setId(savedCosecha.getId()); // Ahora sabemos que no es null
        messageDTO.setProducto(savedCosecha.getProducto());
        messageDTO.setTonelada(savedCosecha.getToneladas());

        // Enviar el DTO a cola_nueva_cosecha (para inventario)
        logger.info("Enviando mensaje a cola_nueva_cosecha: id={}, producto={}",
                messageDTO.getId(), messageDTO.getProducto());
        rabbitTemplate.convertAndSend("agroflow_exchange", "cosecha.nueva", messageDTO);

        // TAMBIÉN enviar el DTO a cola_facturacion (para facturación)
        logger.info("Enviando mensaje a cola_facturacion: id={}, producto={}",
                messageDTO.getId(), messageDTO.getProducto());
        rabbitTemplate.convertAndSend("agroflow_exchange", "cosecha.facturar", messageDTO);

        return savedCosecha;
    }

    public void actualizarEstado(Long cosechaId, String estado, String facturaId) {
        logger.info("Actualizando estado de cosecha: id={}, nuevoEstado={}, facturaId={}",
                cosechaId, estado, facturaId);

        Cosecha cosecha = cosechaRepository.findById(cosechaId)
                .orElseThrow(() -> new RuntimeException("Cosecha no encontrada"));

        String estadoAnterior = cosecha.getEstado();
        cosecha.setEstado(estado);
        cosechaRepository.save(cosecha);

        logger.info("Estado de cosecha {} actualizado de '{}' a '{}'",
                cosechaId, estadoAnterior, estado);
    }
}