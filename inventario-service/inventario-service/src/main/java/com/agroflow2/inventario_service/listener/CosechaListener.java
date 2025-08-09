package com.agroflow2.inventario_service.listener;

import com.agroflow2.inventario_service.config.RabbitMQConfig;
import com.agroflow2.inventario_service.dto.CosechaDTO;
import com.agroflow2.inventario_service.service.InventarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CosechaListener {

    private static final Logger logger = LoggerFactory.getLogger(CosechaListener.class);

    @Autowired
    private InventarioService inventarioService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_COSECHA)
    public void procesarNuevaCosecha(CosechaDTO cosechaDTO) {
        logger.info("Mensaje recibido de la cola: {}", cosechaDTO);

        try {
            // Validar que el mensaje tenga los datos mínimos necesarios
            if (cosechaDTO == null) {
                logger.error("Mensaje de cosecha nulo recibido");
                return;
            }

            if (cosechaDTO.getProducto() == null || cosechaDTO.getProducto().isEmpty()) {
                logger.error("Mensaje de cosecha sin producto válido: {}", cosechaDTO);
                return;
            }

            if (cosechaDTO.getTonelada() <= 0) {
                logger.error("Mensaje de cosecha con cantidad inválida: {}", cosechaDTO);
                return;
            }

            logger.info("Procesando cosecha válida: producto={}, cantidad={}",
                    cosechaDTO.getProducto(), cosechaDTO.getTonelada());

            // Procesar la cosecha usando el servicio
            inventarioService.procesarCosecha(cosechaDTO);

            logger.info("Cosecha procesada exitosamente: {}", cosechaDTO.getId());

        } catch (Exception e) {
            logger.error("Error procesando mensaje de cosecha: {}", e.getMessage(), e);
            // Aquí podrías implementar lógica de reintento o envío a cola de error
            throw e; // Re-lanzar para que RabbitMQ maneje el reintento
        }
    }
}