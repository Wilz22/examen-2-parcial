package com.agroflow2.inventario_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static final String QUEUE_COSECHA = "cola_nueva_cosecha";
    public static final String QUEUE_INVENTARIO_AJUSTADO = "cola_inventario_ajustado";
    public static final String EXCHANGE = "agroflow_exchange";
    public static final String ROUTING_KEY_COSECHA = "cosecha.nueva";
    public static final String ROUTING_KEY_INVENTARIO = "inventario.ajustado";

    @Bean
    public Queue colaNuevaCosecha() {
        return new Queue(QUEUE_COSECHA, true);
    }

    @Bean
    public Queue colaInventarioAjustado() {
        return new Queue(QUEUE_INVENTARIO_AJUSTADO, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding bindingCosecha(Queue colaNuevaCosecha, TopicExchange exchange) {
        return BindingBuilder.bind(colaNuevaCosecha).to(exchange).with(ROUTING_KEY_COSECHA);
    }

    @Bean
    public Binding bindingInventario(Queue colaInventarioAjustado, TopicExchange exchange) {
        return BindingBuilder.bind(colaInventarioAjustado).to(exchange).with(ROUTING_KEY_INVENTARIO);
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