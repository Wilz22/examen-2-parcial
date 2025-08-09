package com.agroflow.cosecha_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "agroflow_exchange";
    public static final String QUEUE_NUEVA_COSECHA = "cola_nueva_cosecha";
    public static final String QUEUE_FACTURACION = "cola_facturacion";
    public static final String QUEUE_FACTURACION_COMPLETADA = "cola_facturacion_completada";
    public static final String QUEUE_INVENTARIO_AJUSTADO = "cola_inventario_ajustado";

    public static final String ROUTING_KEY_COSECHA_NUEVA = "cosecha.nueva";
    public static final String ROUTING_KEY_COSECHA_FACTURAR = "cosecha.facturar";
    public static final String ROUTING_KEY_FACTURA_COMPLETADA = "factura.completada";
    public static final String ROUTING_KEY_INVENTARIO_AJUSTADO = "inventario.ajustado";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    // Colas que necesita el servicio de cosecha
    @Bean
    public Queue colaNuevaCosecha() {
        return new Queue(QUEUE_NUEVA_COSECHA, true);
    }

    @Bean
    public Queue colaFacturacion() {
        return new Queue(QUEUE_FACTURACION, true);
    }

    @Bean
    public Queue colaFacturacionCompletada() {
        return new Queue(QUEUE_FACTURACION_COMPLETADA, true);
    }

    @Bean
    public Queue colaInventarioAjustado() {
        return new Queue(QUEUE_INVENTARIO_AJUSTADO, true);
    }

    // Bindings
    @Bean
    public Binding bindingNuevaCosecha(Queue colaNuevaCosecha, TopicExchange exchange) {
        return BindingBuilder.bind(colaNuevaCosecha).to(exchange).with(ROUTING_KEY_COSECHA_NUEVA);
    }

    @Bean
    public Binding bindingFacturacion(Queue colaFacturacion, TopicExchange exchange) {
        return BindingBuilder.bind(colaFacturacion).to(exchange).with(ROUTING_KEY_COSECHA_FACTURAR);
    }

    @Bean
    public Binding bindingFacturacionCompletada(Queue colaFacturacionCompletada, TopicExchange exchange) {
        return BindingBuilder.bind(colaFacturacionCompletada).to(exchange).with(ROUTING_KEY_FACTURA_COMPLETADA);
    }

    @Bean
    public Binding bindingInventarioAjustado(Queue colaInventarioAjustado, TopicExchange exchange) {
        return BindingBuilder.bind(colaInventarioAjustado).to(exchange).with(ROUTING_KEY_INVENTARIO_AJUSTADO);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}