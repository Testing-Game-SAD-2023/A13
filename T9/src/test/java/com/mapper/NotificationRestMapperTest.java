package com.model.mapper;

import com.model.Notification;
import com.model.dto.NotificationRestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class NotificationRestMapperTest {

    private final NotificationRestMapper mapper = new NotificationRestMapper();

    @Test
    void testToDTO_HappyPath() {
        // 1. ARRANGE
        // Simuliamo una entità che arriva dal DB (quindi ha ID e Date già settate)
        Notification entity = new Notification();
        entity.setId(99L); // Supponendo tu abbia il setter per l'ID
        entity.setUserId(10L);
        entity.setType("ERROR");
        entity.setTitle("Server Down");
        entity.setBody("Critical error");
        entity.setRead(true);
        // Nota: createdAt viene settato nel costruttore, ma possiamo fidarci di quello o settarne uno fisso se hai il setter

        // 2. ACT
        NotificationRestDTO dto = mapper.toDTO(entity);

        // 3. ASSERT
        Assertions.assertEquals(99L, dto.getId());
        Assertions.assertEquals(10L, dto.getUserId());
        Assertions.assertEquals("ERROR", dto.getType());
        Assertions.assertEquals("Server Down", dto.getTitle());
        Assertions.assertEquals("Critical error", dto.getBody());
        Assertions.assertTrue(dto.isRead());

        // Verifichiamo che la data sia stata copiata (non null)
        Assertions.assertNotNull(dto.getCreatedAt());
        // Se vuoi essere preciso: Assertions.assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
    }
}
