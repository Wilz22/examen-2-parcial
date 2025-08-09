package com.agroflow2.inventario_service.controller;

import com.agroflow2.inventario_service.config.RabbitMQConfig;
import com.agroflow2.inventario_service.dto.CosechaDTO;
import com.agroflow2.inventario_service.model.Insumo;
import com.agroflow2.inventario_service.service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/inventario")
public class InventarioController {

    private static final Logger logger = LoggerFactory.getLogger(InventarioController.class);

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/stock/{nombreInsumo}")
    public Insumo obtenerStock(@PathVariable String nombreInsumo) {
        logger.info("Obteniendo stock para insumo: {}", nombreInsumo);
        return inventarioService.obtenerInsumoPorNombre(nombreInsumo);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_COSECHA)
    public void consumirCosecha(Map<String, Object> message) {
        try {
            logger.info("Mensaje recibido de la cola: {}", message);
            // Mapear los campos del mensaje al CosechaDTO del servicio de inventario
            CosechaDTO cosechaDTO = new CosechaDTO();
            cosechaDTO.setId((String) message.get("id"));
            cosechaDTO.setProducto((String) message.get("producto"));
            cosechaDTO.setTonelada(((Number) message.get("tonelada")).doubleValue()); // Mapear tonelada a cantidad

            logger.info("CosechaDTO mapeado: id={}, producto={}, cantidad={}",
                    cosechaDTO.getId(), cosechaDTO.getProducto(), cosechaDTO.getTonelada());

            inventarioService.procesarCosecha(cosechaDTO);
        } catch (Exception e) {
            logger.error("Error al procesar el mensaje de cosecha: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar el mensaje de cosecha: " + e.getMessage(), e);
        }
    }
}