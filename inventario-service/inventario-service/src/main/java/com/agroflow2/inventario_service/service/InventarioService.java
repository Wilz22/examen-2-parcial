package com.agroflow2.inventario_service.service;

import com.agroflow2.inventario_service.config.RabbitMQConfig;
import com.agroflow2.inventario_service.dto.CosechaDTO;
import com.agroflow2.inventario_service.dto.InsumoAjustadoDTO;
import com.agroflow2.inventario_service.model.Insumo;
import com.agroflow2.inventario_service.repository.InsumoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);
    private static final double STOCK_INICIAL = 200.0;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void procesarCosecha(CosechaDTO cosechaDTO) {
        logger.info("Iniciando procesamiento de cosecha: id={}, producto={}, cantidad={}",
                cosechaDTO.getId(), cosechaDTO.getProducto(), cosechaDTO.getTonelada());

        String nombreInsumo = cosechaDTO.getProducto();
        double cantidadCosecha = cosechaDTO.getTonelada();

        // Validar entrada
        if (nombreInsumo == null || nombreInsumo.isEmpty()) {
            logger.error("El nombre del insumo no puede ser nulo o vacío");
            throw new IllegalArgumentException("El nombre del insumo no puede ser nulo o vacío");
        }
        if (cantidadCosecha <= 0) {
            logger.error("La cantidad de cosecha debe ser mayor a 0: {}", cantidadCosecha);
            throw new IllegalArgumentException("La cantidad de cosecha debe ser mayor a 0");
        }

        // Buscar si el insumo ya existe
        Insumo insumo = insumoRepository.findByNombreInsumo(nombreInsumo);
        double nuevoStock;

        if (insumo == null) {
            // Si el insumo no existe, crear uno nuevo con stock inicial de 200 MENOS la cantidad cosechada
            logger.info("Insumo {} no encontrado, creando nuevo con stock inicial {} menos cantidad cosechada {}",
                    nombreInsumo, STOCK_INICIAL, cantidadCosecha);
            nuevoStock = STOCK_INICIAL - cantidadCosecha;

            // Validar que el stock inicial sea suficiente
            if (nuevoStock < 0) {
                logger.error("Stock inicial insuficiente para el nuevo insumo: {}. Stock inicial: {}, Cantidad requerida: {}",
                        nombreInsumo, STOCK_INICIAL, cantidadCosecha);
                throw new RuntimeException("Stock inicial insuficiente para el nuevo insumo: " + nombreInsumo);
            }

            insumo = new Insumo(null, nombreInsumo, nuevoStock);
        } else {
            // Si el insumo existe, RESTAR la cantidad de la cosecha del stock actual
            logger.info("Insumo {} encontrado con stock actual {}",
                    nombreInsumo, insumo.getStock());
            nuevoStock = insumo.getStock() - cantidadCosecha;

            // Validar que haya stock suficiente
            if (nuevoStock < 0) {
                logger.error("Stock insuficiente para el insumo: {}. Stock actual: {}, Cantidad requerida: {}",
                        nombreInsumo, insumo.getStock(), cantidadCosecha);
                throw new RuntimeException("Stock insuficiente para el insumo: " + nombreInsumo);
            }

            insumo.setStock(nuevoStock);
        }

        logger.info("Actualizando stock para insumo {}: nuevo stock = {}", nombreInsumo, nuevoStock);

        try {
            // Guardar el insumo en la base de datos
            Insumo savedInsumo = insumoRepository.save(insumo);
            entityManager.flush(); // Forzar el flush para asegurar que se persista
            logger.info("Insumo guardado exitosamente: id={}, nombre={}, stock={}",
                    savedInsumo.getId(), savedInsumo.getNombreInsumo(), savedInsumo.getStock());

            // Publicar evento en cola_inventario_ajustado
            InsumoAjustadoDTO ajusteDTO = new InsumoAjustadoDTO(
                    cosechaDTO.getId(),
                    nombreInsumo,
                    cantidadCosecha,
                    nuevoStock
            );
            logger.info("Enviando evento de ajuste a RabbitMQ: {}", ajusteDTO);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_INVENTARIO, ajusteDTO);

            logger.info("Procesamiento de cosecha completado exitosamente");
        } catch (Exception e) {
            logger.error("Error al guardar el insumo o enviar el evento: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar la cosecha: " + e.getMessage(), e);
        }
    }

    public Insumo obtenerInsumoPorNombre(String nombreInsumo) {
        logger.info("Buscando insumo por nombre: {}", nombreInsumo);
        if (nombreInsumo == null || nombreInsumo.isEmpty()) {
            logger.error("El nombre del insumo no puede ser nulo o vacío");
            throw new IllegalArgumentException("El nombre del insumo no puede ser nulo o vacío");
        }

        Insumo insumo = insumoRepository.findByNombreInsumo(nombreInsumo);
        if (insumo == null) {
            logger.error("Insumo no encontrado: {}", nombreInsumo);
            throw new RuntimeException("Insumo no encontrado: " + nombreInsumo);
        }
        logger.info("Insumo encontrado: id={}, nombre={}, stock={}",
                insumo.getId(), insumo.getNombreInsumo(), insumo.getStock());
        return insumo;
    }
}