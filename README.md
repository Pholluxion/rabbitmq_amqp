## Plataforma de Pagos

**Descripción:**

Este proyecto implementa una plataforma de pagos usando una arquitectura de microservicios con RabbitMQ, Docker, Quarkus y Spring Boot. La plataforma tiene 2 servicios publicadores y 2 suscriptores.

**Componentes:**

* **Servicio Publicador 1 (payment_request_service):**
    * Implementado con Quarkus.
    * Exporta una API REST para recibir solicitudes de pago.
    * Envía mensajes a la cola `payments` en RabbitMQ.
* **Servicio Publicador 2 (payment_processor_service):**
    * Implementado con Quarkus.
    * Se conecta a la cola `payments` en RabbitMQ.
    * Procesa pagos con tarjeta de crédito utilizando un proveedor de pagos externo.
    * Envía mensajes a la cola `payment_results` en RabbitMQ con el resultado del pago.
* **Servicio Suscriptor 1 (payment_notification_service):**
    * Implementado con  Spring Boot.
    * Se conecta a la cola `payment_results` en RabbitMQ.
    * Notifica al cliente sobre el pago exitoso a través de correo electrónico o SMS.
* **Servicio Suscriptor 2 (order_update_service):**
    * Implementado con Spring Boot.
    * Se conecta a la cola `payment_results` en RabbitMQ.
    * Actualiza el estado del pedido en el sistema de gestión.
* **RabbitMQ:**
    * Implementa un sistema de mensajería asíncrono.
    * Se utiliza para la comunicación entre los microservicios.
    * Dos colas: `payments` y `payment_results`.
* **Docker:**
    * Se utiliza para contenerizar y desplegar los microservicios.
    * Cada microservicio se ejecuta en su propio contenedor.

**Flujo de trabajo:**

1. El cliente envía una solicitud de pago al `payment_request_service`.
2. `payment_request_service` envía un mensaje a la cola `payments` en RabbitMQ.
3. `payment_processor_service` recibe el mensaje de la cola y procesa el pago con tarjeta de crédito.
4. `payment_processor_service` envía un mensaje a la cola `payment_results` con el resultado del pago.
5. `payment_notification_service ` recibe el mensaje de la cola y notifica al cliente sobre el pago exitoso.
6. `order_update_service` recibe el mensaje de la cola y actualiza el estado del pedido en el sistema de gestión.

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
  
