# AgroFlow - Sistema Distribuido de Gestión Agrícola

Este proyecto implementa un sistema distribuido basado en microservicios para la gestión de procesos agrícolas, incluyendo módulos de cosecha, facturación e inventario. Utiliza Docker, Kubernetes, RabbitMQ y PostgreSQL para garantizar escalabilidad y robustez.

## Estructura del Proyecto

- `cosecha-service/`: Microservicio para la gestión de cosechas.
- `facturacion-service/`: Microservicio para la facturación.
- `inventario-service/`: Microservicio para el inventario.
- `kubernetes/`: Archivos de despliegue para Kubernetes.
- `docker-compose.yml`: Orquestación de servicios para desarrollo local.

## Tecnologías Utilizadas

- Java (Spring Boot)
- Docker & Docker Compose
- Kubernetes
- RabbitMQ (mensajería)
- PostgreSQL (base de datos)

## Requisitos Previos

- Docker y Docker Compose instalados
- Java 17+ y Maven (para desarrollo)
- Kubernetes (Minikube, Docker Desktop o similar, para despliegue)

## Ejecución Local

1. Clona el repositorio:

   ```sh
   git clone https://github.com/Wilz22/examen-2-parcial.git
   cd examen-2-parcial
   ```

2. Levanta los servicios con Docker Compose:

   ```sh
   docker-compose up --build
   ```

3. Los servicios estarán disponibles en los siguientes puertos (por defecto):
   - Cosecha: 8081
   - Facturación: 8082
   - Inventario: 8083
   - RabbitMQ: 5672 (AMQP), 15672 (UI)
   - PostgreSQL: 5432

## Despliegue en Kubernetes

1. Asegúrate de tener un clúster de Kubernetes corriendo.
2. Aplica los manifiestos:
   ```sh
   kubectl apply -f kubernetes/
   ```

## Arquitectura

- Comunicación entre microservicios mediante RabbitMQ.
- Persistencia de datos en PostgreSQL.
- Cada microservicio es independiente y escalable.

## Autores
- Jeremmy Varela -
- Johanna Moncayo
- Santiago Llumiquinga
