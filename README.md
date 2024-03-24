## Plataforma de Pagos

**Descripción:**

Este proyecto implementa una plataforma de pagos usando una arquitectura de microservicios con RabbitMQ, Docker, Quarkus y Spring Boot. La plataforma tiene 2 servicios publicadores y 2 suscriptores.

**Componentes:**

* **Servicio Publicador 1 (payment_request_service):**
    * Implementado con Quarkus.
    * Exporta una API REST para recibir solicitudes de pago.
    * Envía mensajes a la cola `payments-requests` en RabbitMQ.
* **Servicio Publicador 2 (payment_processor_service):**
    * Implementado con Quarkus.
    * Se conecta a la cola `payments-requests` en RabbitMQ.
    * Procesa pagos con tarjeta de crédito utilizando un proveedor de pagos externo.
    * Envía mensajes a la cola `payments` en RabbitMQ con el resultado del pago.
* **Servicio Suscriptor 1 (payment_notification_service):**
    * Implementado con  Spring Boot.
    * Se conecta a la cola `payments` en RabbitMQ.
    * Notifica al cliente sobre el pago exitoso a través de correo electrónico o SMS.
* **Servicio Suscriptor 2 (order_update_service):**
    * Implementado con Spring Boot.
    * Se conecta a la cola `payments` en RabbitMQ.
    * Actualiza el estado del pedido en el sistema de gestión.
* **RabbitMQ:**
    * Implementa un sistema de mensajería asíncrono.
    * Se utiliza para la comunicación entre los microservicios.
    * Dos colas: `payments` y `payments-requests`.
* **Docker:**
    * Se utiliza para contenerizar y desplegar los microservicios.
    * Cada microservicio se ejecuta en su propio contenedor.

**Flujo de trabajo:**

1. El cliente envía una solicitud de pago al `payment_request_service`.
2. `payment_request_service` envía un mensaje a la cola `payments-requests` en RabbitMQ.
3. `payment_processor_service` recibe el mensaje de la cola `payments-requests` y procesa el pago.
4. `payment_processor_service` envía un mensaje a la cola `payments` con el resultado del pago.
5. `payment_notification_service ` recibe el mensaje de la cola `payments` y notifica al cliente sobre el pago exitoso.
6. `order_update_service` recibe el mensaje de la cola `payments` y actualiza el estado del pedido en el sistema de gestión.

**Tecnologías:**

* **Arquitectura de Microservicios:** Permite un desarrollo modular y escalable.
* **RabbitMQ:** Implementa un sistema de mensajería asíncrono para la comunicación entre servicios.
* **Docker:** Facilita la contenerización y el despliegue de los microservicios.
* **Quarkus:** Framework Java para el desarrollo de microservicios nativos de la nube.
* **Spring Boot:** Framework Java para el desarrollo rápido y fácil de aplicaciones Spring.

**Recursos adicionales:**

* **RabbitMQ: [https://www.rabbitmq.com/docs](https://www.rabbitmq.com/docs)**
* **Docker: [https://docs.docker.com/](https://docs.docker.com/)**
* **Quarkus: [https://quarkus.io/](https://quarkus.io/)**
* **Spring Boot: [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)**


### Pre-requisitos

* **Docker:** Instalado y funcionando ([https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/))
* **Docker Compose (Opcional, Recomendado):** Para administrar todos los servicios ([https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/))
* **Herramienta de Construcción:**
  * Maven (si el proyecto usa Maven)
  * Gradle (si el proyecto usa Gradle)

### Construyendo Microservicios

1. **Navega al directorio del microservicio (por ejemplo, payment_processor_service):**

   ```
   cd payment_processor_service
   ```

2. **Empaqueta el servicio (si usa Maven):**

   ```
   ./mvnw package
   ```

3. **Construye la imagen de Docker:**

   ```
   docker build -f src/main/docker/Dockerfile.jvm -t rabbitmq/payment_processor_service .
   ```

  - Esto construye una imagen llamada `rabbitmq/payment_processor_service` usando el Dockerfile ubicado en `src/main/docker/Dockerfile.jvm`. El contexto (directorio actual) se indica con `.` al final.

4. **Repite los pasos 1-3 para cada directorio de microservicio:**

  - `payment_request_service` (usando la herramienta de construcción adecuada)
  - `order_update_service`
  - `payment_notification_service`

### Creando la Red RabbitMQ

1. **Crea una red Docker llamada `rabbitmq-net`:**

   ```
   docker network create rabbitmq-net
   ```

### Desplegando los Servicios

**Opción 1: Usando Comandos Individuales de Docker**

1. **Ejecuta el contenedor RabbitMQ (opcional, se recomienda usar Docker Compose):**

   ```
   docker run --network rabbitmq-net -d -p15672:15672 -p 5672:5672 --hostname my-rabbit --name some-rabbit rabbitmq:3-management
   ```

  - Este comando inicia un contenedor RabbitMQ llamado `some-rabbit` con los puertos 15672 y 5672 mapeados a la máquina host para acceso de administración. Las opciones `--hostname` y `--name` son para mayor claridad.

2. **Ejecuta los contenedores de microservicios:**

   ```
   docker run --network rabbitmq-net -d -p8080:8080 --name payment_request_service rabbitmq/payment_request_service
   docker run --network rabbitmq-net -d -p8081:8081 --name payment_processor_service rabbitmq/payment_processor_service
   docker run --network rabbitmq-net -d -p8082:8082 --name order_update_service rabbitmq/order_update_service
   docker run --network rabbitmq-net -d -p8083:8083 --name payment_notification_service rabbitmq/payment_notification_service
   ```

  - Estos comandos ejecutan cada contenedor de microservicio en modo desacoplado (`-d`) con asignaciones de puertos específicas (`-p`). Los nombres de los contenedores son para mayor claridad.

**Opción 2: Usando Docker Compose (Recomendado)**

1. **Crea un archivo `docker-compose.yml` con el siguiente contenido:**

   ```yaml
    version: "3.8"
    
    services:
      rabbitmq:
        image: rabbitmq:3-management
        ports:
          - "15672:15672"
          - "5672:5672"
        networks:
          - rabbitmq-net
    
      payment_request_service:
        image: rabbitmq/payment_request_service
        ports:
          - "8080:8080"
        networks:
          - rabbitmq-net
    
      payment_processor_service:
        image: rabbitmq/payment_processor_service
        ports:
          - "8081:8081"
        networks:
          - rabbitmq-net
    
      order_update_service:
        image: rabbitmq/order_update_service
        ports:
          - "8082:8082"
        networks:
          - rabbitmq-net
    
      payment_notification_service:
        image: rabbitmq/payment_notification_service
        ports:
          - "8083:8083"
        networks:
          - rabbitmq-net
    
    networks:
      rabbitmq-net:
        external: true
    ```

