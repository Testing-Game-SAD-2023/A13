package com.communication;

import com.a13.notification.client.dto.NotificationResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ReplyProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ReplyProducer producer;

    @Test
    void testSendReply_Success() {
        String queue = "reply.queue";
        NotificationResponseDTO response = new NotificationResponseDTO(1L, "OK", "Msg");

        producer.sendReply(queue, response);

        // Verifica che RabbitTemplate sia stato chiamato con la coda giusta
        verify(rabbitTemplate).convertAndSend(eq(queue), eq(response));
    }

    @Test
    void testSendReply_NullQueue() {
        // Se la coda è null, non deve fare nulla
        producer.sendReply(null, new NotificationResponseDTO());

        // Verifica: nessuna interazione con RabbitTemplate
        verifyNoInteractions(rabbitTemplate);
    }
}
