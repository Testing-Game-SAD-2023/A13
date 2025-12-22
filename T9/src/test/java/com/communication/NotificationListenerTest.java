package com.communication;

import com.a13.notification.client.dto.NotificationDTO;
import com.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationListenerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationListener listener;

    @Test
    void testHandleNotificationMessage_Success() {
        // 1. Dati di prova
        NotificationDTO dto = new NotificationDTO();
        dto.setUserId(1L);

        // 2. Chiamata al metodo
        listener.handleNotificationMessage(dto);

        // 3. Verifica: deve aver passato il DTO al service
        verify(notificationService).processIncomingNotification(dto);
    }

    @Test
    void testHandleNotificationMessage_ExceptionSafe() {
        // Simuliamo che il service lanci un errore
        NotificationDTO dto = new NotificationDTO();
        doThrow(new RuntimeException("Service error")).when(notificationService).processIncomingNotification(dto);

        // Chiamiamo il metodo: NON deve lanciare eccezioni (grazie al try-catch)
        listener.handleNotificationMessage(dto);

        // Verifica che abbia provato a chiamare il service
        verify(notificationService).processIncomingNotification(dto);
    }
}
