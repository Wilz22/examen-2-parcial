package com.agroflow3.facturacion_service.service;

import com.agroflow3.facturacion_service.config.RabbitMQConfig;
import com.agroflow3.facturacion_service.dto.CosechaDTO;
import com.agroflow3.facturacion_service.dto.FacturaMessage;
import com.agroflow3.facturacion_service.model.Factura;
import com.agroflow3.facturacion_service.repository.FacturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacturaService {

    private static final Logger logger = LoggerFactory.getLogger(FacturaService.class);
    private static final Map<String, Double> PRECIOS = new HashMap<>();

    static {
        PRECIOS.put("Arroz", 120.0);
        PRECIOS.put("Azucar", 150.0);
        PRECIOS.put("Café", 300.0);
        PRECIOS.put("Maíz", 100.0);
    }

    @Autowired
    private FacturaRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<Factura> listarTodas() {
        return repository.findAll();
    }

    public void procesarCosecha(CosechaDTO dto) {
        logger.info("Procesando facturación para cosecha: {}", dto);

        try {
            // Calcular el monto
            Double precio = PRECIOS.getOrDefault(dto.getProducto(), 100.0); // Precio por defecto: $100
            Double monto = dto.getCantidad() * precio;

            logger.info("Calculando factura: producto={}, cantidad={}, precio={}, monto={}",
                    dto.getProducto(), dto.getCantidad(), precio, monto);

            // Crear y guardar la factura
            Factura factura = new Factura();
            factura.setCosechaId(dto.getCosechaId());
            factura.setMonto(monto);
            factura.setPagada(false);
            Factura savedFactura = repository.save(factura);

            logger.info("Factura guardada: id={}, cosechaId={}, monto={}",
                    savedFactura.getFacturaId(), savedFactura.getCosechaId(), savedFactura.getMonto());

            // Crear mensaje para notificar factura completada
            FacturaMessage facturaMessage = new FacturaMessage();
            facturaMessage.setCosechaId(dto.getCosechaId());
            facturaMessage.setFacturaId(savedFactura.getFacturaId());

            // Enviar mensaje a cola_facturacion_completada usando RabbitMQ
            logger.info("Enviando mensaje de factura completada a RabbitMQ: {}", facturaMessage);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY_FACTURA_COMPLETADA,
                    facturaMessage
            );

            logger.info("Facturación completada exitosamente para cosecha {}", dto.getCosechaId());

        } catch (Exception e) {
            logger.error("Error procesando facturación para cosecha {}: {}", dto.getCosechaId(), e.getMessage(), e);
            throw new RuntimeException("Error procesando facturación", e);
        }
    }
}