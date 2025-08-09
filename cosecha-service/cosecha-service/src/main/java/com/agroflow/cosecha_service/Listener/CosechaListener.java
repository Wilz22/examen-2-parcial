package com.agroflow.cosecha_service.Listener;

import com.agroflow.cosecha_service.dto.InsumoAjustadoDTO;
import com.agroflow.cosecha_service.model.FacturaMessage;
import com.agroflow.cosecha_service.service.CosechaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CosechaListener {

    private static final Logger logger = LoggerFactory.getLogger(CosechaListener.class);
    private final CosechaService cosechaService;

    public CosechaListener(CosechaService cosechaService) {
        this.cosechaService = cosechaService;
    }

    @RabbitListener(queues = "cola_facturacion_completada")
    public void handleFacturacionMessage(FacturaMessage message) {
        logger.info("Recibido mensaje de facturación: {}", message);

        // Validar que el mensaje tenga los datos necesarios
        if (message == null) {
            logger.error("Mensaje de facturación es null");
            return;
        }

        String cosechaIdStr = message.getCosechaId();
        if (cosechaIdStr == null || cosechaIdStr.trim().isEmpty()) {
            logger.error("CosechaId es null o vacío en el mensaje: {}", message);
            return;
        }

        try {
            Long cosechaId = Long.parseLong(cosechaIdStr.trim());
            cosechaService.actualizarEstado(
                    cosechaId,
                    "FACTURADA",
                    message.getFacturaId()
            );
            logger.info("Estado de cosecha {} actualizado a FACTURADA", cosechaId);
        } catch (NumberFormatException e) {
            logger.error("Error al parsear cosechaId '{}': {}", cosechaIdStr, e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar estado de cosecha: {}", e.getMessage());
        }
    }

    @RabbitListener(queues = "cola_inventario_ajustado")
    public void handleInventarioAjustadoMessage(InsumoAjustadoDTO insumoAjustadoDTO) {
        logger.info("=== STOCK ACTUALIZADO ===");
        logger.info("Cosecha ID: {}", insumoAjustadoDTO.getCosechaId());
        logger.info("Producto/Insumo: {}", insumoAjustadoDTO.getInsumo());
        logger.info("Cantidad Deducida: {} toneladas", insumoAjustadoDTO.getCantidadDeducida());
        logger.info("Nuevo Stock Disponible: {} toneladas", insumoAjustadoDTO.getNuevoStock());
        logger.info("========================");

        // Mensaje adicional en la terminal
        System.out.println("🌾 STOCK ACTUALIZADO:");
        System.out.println("   📦 Producto: " + insumoAjustadoDTO.getInsumo());
        System.out.println("   📉 Cantidad procesada: " + insumoAjustadoDTO.getCantidadDeducida() + " toneladas");
        System.out.println("   📊 Stock restante: " + insumoAjustadoDTO.getNuevoStock() + " toneladas");
        System.out.println("   🆔 Cosecha ID: " + insumoAjustadoDTO.getCosechaId());
        System.out.println("─────────────────────────────────────────");
    }
}