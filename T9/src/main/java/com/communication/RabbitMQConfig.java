package communication;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nomi standard (puoi cambiarli, ma così sono perfetti)
    public static final String NOTIFICATION_EXCHANGE = "notifications.exchange";
    public static final String NOTIFICATION_QUEUE = "notifications.create.queue";
    public static final String ROUTING_KEY = "notifications.create";

    @Bean
    public Queue notificationQueue() {
        // true = durable  → la coda sopravvive ai restart
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with(ROUTING_KEY);
    }
}
