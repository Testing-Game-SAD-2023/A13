package com.service;

import com.a13.notification.client.dto.NotificationDTO;
import com.a13.notification.client.dto.NotificationResponseDTO;
import com.communication.ReplyProducer;
import com.mapper.NotificationMapper;
import com.model.Notification;
import com.model.dto.NotificationRestDTO;
import com.model.mapper.NotificationRestMapper;
import com.model.repository.NotificationRepository;
import com.service.sse.SseConnectionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Abilita Mockito
class NotificationServiceTest {

    // 1. CREIAMO I MOCK (Le dipendenze finte)
    @Mock private NotificationRepository notificationRepository;
    @Mock private NotificationMapper notificationMapper;
    @Mock private ReplyProducer replyProducer;
    @Mock private NotificationRestMapper restMapper;
    @Mock private SseConnectionManager sseManager;

    // 2. INIETTIAMO I MOCK NEL SERVIZIO REALE
    @InjectMocks
    private NotificationService notificationService;

    // =========================================================================
    // TEST: processIncomingNotification (Il cuore del sistema)
    // =========================================================================

    @Test
    void testProcessIncomingNotification_Success() {
        // ARRANGE (Preparo lo scenario)
        NotificationDTO inputDto = new NotificationDTO();
        inputDto.setUserId(1L);
        inputDto.setType("INFO");
        inputDto.setReplyTo("queue.reply"); // Importante: ha una coda di risposta

        Notification entity = new Notification();
        entity.setUserId(1L);

        Notification savedEntity = new Notification();
        savedEntity.setId(100L); // Simuliamo che il DB abbia assegnato ID 100
        savedEntity.setUserId(1L);

        NotificationRestDTO restDTO = new NotificationRestDTO(100L, 1L, "INFO", "T", "B", false, null);

        // Addestro i Mock a rispondere come voglio
        when(notificationMapper.toEntity(inputDto)).thenReturn(entity);
        when(notificationRepository.save(entity)).thenReturn(savedEntity);
        when(restMapper.toDTO(savedEntity)).thenReturn(restDTO);

        // ACT (Eseguo il metodo)
        notificationService.processIncomingNotification(inputDto);

        // ASSERT (Verifico che il servizio abbia chiamato i metodi giusti)

        // 1. Deve aver salvato sul DB
        verify(notificationRepository).save(entity);

        // 2. Deve aver mandato l'evento SSE all'utente giusto
        verify(sseManager).dispatch(eq(1L), eq(restDTO));

        // 3. Deve aver mandato la conferma via RabbitMQ (ReplyProducerTest.java)
        // Catturiamo l'argomento per controllare cosa gli ha passato
        ArgumentCaptor<NotificationResponseDTO> responseCaptor = ArgumentCaptor.forClass(NotificationResponseDTO.class);
        verify(replyProducer).sendReply(eq("queue.reply"), responseCaptor.capture());

        NotificationResponseDTO sentResponse = responseCaptor.getValue();
        Assertions.assertEquals("OK", sentResponse.getStatus());
        Assertions.assertEquals(100L, sentResponse.getNotificationId());
    }

    @Test
    void testProcessIncomingNotification_ErrorHandling() {
        // ARRANGE: Simuliamo un errore del DB
        NotificationDTO inputDto = new NotificationDTO();
        inputDto.setReplyTo("queue.reply");

        when(notificationMapper.toEntity(any())).thenThrow(new RuntimeException("DB Error"));

        // ACT
        notificationService.processIncomingNotification(inputDto);

        // ASSERT: Verifico che non sia esploso, ma abbia mandato un messaggio di ERRORE
        ArgumentCaptor<NotificationResponseDTO> responseCaptor = ArgumentCaptor.forClass(NotificationResponseDTO.class);
        verify(replyProducer).sendReply(eq("queue.reply"), responseCaptor.capture());

        NotificationResponseDTO sentResponse = responseCaptor.getValue();
        Assertions.assertEquals("ERROR", sentResponse.getStatus());
        Assertions.assertTrue(sentResponse.getMessage().contains("DB Error"));
    }

    // =========================================================================
    // TEST: markNotificationAsRead (Logica di business e sicurezza)
    // =========================================================================

    @Test
    void testMarkNotificationAsRead_Success() {
        // ARRANGE
        Long userId = 50L;
        Long notifId = 10L;

        Notification existing = new Notification();
        existing.setId(notifId);
        existing.setUserId(userId);
        existing.setRead(false);

        // Simuliamo che il DB trovi la notifica
        when(notificationRepository.findById(notifId)).thenReturn(Optional.of(existing));

        // ACT
        notificationService.markNotificationAsRead(userId, notifId);

        // ASSERT
        Assertions.assertTrue(existing.isRead(), "Lo stato deve essere cambiato a true");
        verify(notificationRepository).save(existing); // Deve salvare il cambio di stato
    }

    @Test
    void testMarkNotificationAsRead_WrongUser() {
        // ARRANGE: Notifica di un altro utente
        Long userId = 50L;   // Io sono utente 50
        Long ownerId = 99L;  // La notifica è di utente 99

        Notification existing = new Notification();
        existing.setUserId(ownerId);

        when(notificationRepository.findById(10L)).thenReturn(Optional.of(existing));

        // ACT & ASSERT: Mi aspetto un'eccezione
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            notificationService.markNotificationAsRead(userId, 10L);
        }, "Deve lanciare eccezione se la notifica non è dell'utente");

        // Verifico che NON abbia salvato nulla
        verify(notificationRepository, never()).save(any());
    }
}
