package com.agroflow3.facturacion_service.listener;

import com.agroflow3.facturacion_service.dto.CosechaDTO;
import com.agroflow3.facturacion_service.service.FacturaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CosechaListener {

    private static final Logger logger = LoggerFactory.getLogger(CosechaListener.class);

    @Autowired
    private FacturaService service;

    // Escucha cola_facturacion para procesar cosechas que necesitan facturación
    @RabbitListener(queues = "cola_facturacion")
    public void recibirCosecha(CosechaDTO cosechaDTO) {
        try {
            logger.info("Recibida cosecha para facturación: {}", cosechaDTO);

            // Usar métodos helper para obtener datos
            String cosechaId = cosechaDTO.getIdAsString();
            String producto = cosechaDTO.getProducto();
            Double cantidad = cosechaDTO.getCantidadTotal();

            // Validaciones
            if (cosechaId == null || cosechaId.trim().isEmpty()) {

                return;
            }

            if (producto == null || producto.trim().isEmpty()) {

                return;
            }

            if (cantidad == null || cantidad <= 0) {
                return;
            }

            // Crear DTO para procesar con datos validados
            CosechaDTO dtoParaFacturar = new CosechaDTO();
            dtoParaFacturar.setCosechaId(cosechaId);
            dtoParaFacturar.setProducto(producto);
            dtoParaFacturar.setCantidad(cantidad);

            logger.info("Procesando facturación para cosechaId: {}, producto: {}, cantidad: {}",
                    cosechaId, producto, cantidad);

            service.procesarCosecha(dtoParaFacturar);
            logger.info("Cosecha {} procesada para facturación exitosamente", cosechaId);

        } catch (Exception e) {
            logger.error("Error procesando cosecha para facturación: {}", e.getMessage(), e);
        }
    }
}