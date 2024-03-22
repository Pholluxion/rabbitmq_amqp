package com.phollux.notification;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.logging.Logger;

@Configuration
public class Config {

    static final String fanoutExchangeName = "payments";

    static final String queueName = "payments";

    @Bean
    public Declarables bindings() {
        Queue fanoutQueue = new Queue(queueName, true, false, false, Map.of(
                "x-queue-type", "stream"
        ));

        FanoutExchange fanoutExchange = new FanoutExchange(fanoutExchangeName);

        return new Declarables(
                fanoutQueue,
                fanoutExchange,
                BindingBuilder.bind(fanoutQueue).to(fanoutExchange))
                ;
    }


    @RabbitListener(queues = queueName)
    public void receiveMessageFrom(String message) {

        Logger logger = Logger.getLogger(Config.class.getName());

        final String messageToPrint = "A new notification has been sent: " + message;
        logger.info(messageToPrint);
    }


}
