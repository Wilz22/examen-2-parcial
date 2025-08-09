# Usa una imagen base de Java
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .jar generado desde el directorio target al contenedor
COPY target/facturacion-service-0.0.1-SNAPSHOT.jar /app/app.jar

# Expone el puerto que usa Eureka (por defecto 8761)
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]