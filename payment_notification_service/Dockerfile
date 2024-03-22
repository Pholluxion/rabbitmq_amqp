# Utilizamos el jre de eclipse temurin
FROM eclipse-temurin:17-jre-alpine

# Definimos la variable de entorno de rabbitmq
ENV RABBITMQ_HOST=rabbitmq

# Copiamos el jar ejecutable de la imagen
COPY build/libs/payment_notification_service-0.0.1-SNAPSHOT.jar app.jar

# Lanzamos el ejecutable usando java
ENTRYPOINT [ "java", "-jar", "app.jar" ]

# Exponemos el puerto 8083
EXPOSE 8083