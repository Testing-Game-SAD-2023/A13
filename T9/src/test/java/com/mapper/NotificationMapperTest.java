package com.mapper;

import com.a13.notification.client.dto.NotificationDTO;
import com.model.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NotificationMapperTest {

    // Istanziamo il mapper direttamente (non serve @Autowired qui)
    private final NotificationMapper mapper = new NotificationMapper();

    @Test
    void testToEntity_HappyPath() {
        // 1. ARRANGE (Preparo i dati)
        NotificationDTO dto = new NotificationDTO();
        dto.setUserId(123L);
        dto.setType("INFO");
        dto.setTitle("Test Title");
        dto.setBody("Test Body");

        // 2. ACT (Eseguo il metodo)
        Notification entity = mapper.toEntity(dto);

        // 3. ASSERT (Verifico i risultati)
        Assertions.assertNotNull(entity, "L'entità non deve essere null");
        Assertions.assertEquals(123L, entity.getUserId());
        Assertions.assertEquals("INFO", entity.getType());
        Assertions.assertEquals("Test Title", entity.getTitle());
        Assertions.assertEquals("Test Body", entity.getBody());

        // Verifico i valori di default
        Assertions.assertFalse(entity.isRead(), "Di default isRead deve essere false");
        Assertions.assertNotNull(entity.getCreatedAt(), "La data di creazione deve essere generata dal costruttore dell'Entity");
    }

    @Test
    void testToEntity_NullInput() {
        // Se passo null, mi aspetto null
        Notification result = mapper.toEntity(null);
        Assertions.assertNull(result);
    }

    @Test
    void testToDto_HappyPath() {
        // 1. ARRANGE
        Notification entity = new Notification();
        entity.setUserId(456L);
        entity.setTitle("Entity Title");
        entity.setType("WARNING");

        String replyTo = "queue.response";

        // 2. ACT
        NotificationDTO dto = mapper.toDto(entity, replyTo);

        // 3. ASSERT
        Assertions.assertEquals(456L, dto.getUserId());
        Assertions.assertEquals("Entity Title", dto.getTitle());
        Assertions.assertEquals("WARNING", dto.getType());
        Assertions.assertEquals("queue.response", dto.getReplyTo());
    }
}
