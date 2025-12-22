package com.controller;

import com.model.dto.NotificationRestDTO;
import com.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
@Tag(
        name = "Notifications",
        description = "Gestione delle notifiche utente e sottoscrizione SSE"
)
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ============================
    // SSE SUBSCRIPTION
    // ============================

    @Operation(
            summary = "Sottoscrizione SSE alle notifiche",
            description = "Apre una connessione Server-Sent Events. Il server invierà eventi contenenti oggetti NotificationRestDTO."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Connessione SSE stabilita",
                    content = @Content(
                            mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
                            schema = @Schema(implementation = NotificationRestDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "UserId non valido", content = @Content
            )
    })
    // 👇👇👇 MANCAVA QUESTO! 👇👇👇
    @GetMapping(
            path = "/subscribe/{userId}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter subscribe(
            @Parameter(
                    description = "Identificativo dell'utente",
                    example = "42",
                    required = true
            )
            @PathVariable Long userId
    ) {
        return notificationService.subscribe(userId);
    }


    // ============================
    // GET ALL NOTIFICATIONS
    // ============================

    @Operation(
            summary = "Recupera tutte le notifiche di un utente",
            description = "Restituisce la lista delle notifiche di un utente con filtri opzionali"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista notifiche recuperata con successo",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationRestDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utente non trovato"
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationRestDTO>> getNotifications(

            @Parameter(
                    description = "Identificativo dell'utente",
                    example = "42",
                    required = true
            )
            @PathVariable Long userId,

            @Parameter(
                    description = "Filtro per stato di lettura",
                    example = "false"
            )
            @RequestParam(required = false) Boolean read,

            @Parameter(
                    description = "Filtro per tipo di notifica",
                    example = "INFO"
            )
            @RequestParam(required = false) String type
    ) {
        List<NotificationRestDTO> notifications =
                notificationService.getUserNotifications(userId, read, type);

        return ResponseEntity.ok(notifications);
    }

    // ============================
    // GET SINGLE NOTIFICATION
    // ============================

    @Operation(
            summary = "Recupera una singola notifica",
            description = "Restituisce i dettagli di una notifica dato il suo ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Notifica trovata",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationRestDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notifica non trovata"
            )
    })
    @GetMapping("/{userId}/{notificationId}")
    public ResponseEntity<NotificationRestDTO> getNotification(

            @Parameter(
                    description = "Identificativo dell'utente",
                    example = "42",
                    required = true
            )
            @PathVariable Long userId,

            @Parameter(
                    description = "Identificativo della notifica",
                    example = "1001",
                    required = true
            )
            @PathVariable Long notificationId
    ) {
        NotificationRestDTO dto =
                notificationService.getNotification(userId, notificationId);

        return ResponseEntity.ok(dto);
    }

    // ============================
    // MARK AS READ
    // ============================

    @Operation(
            summary = "Segna una notifica come letta",
            description = "Aggiorna lo stato di una notifica impostandola come letta"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Notifica aggiornata correttamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notifica non trovata"
            )
    })
    @PatchMapping("/{userId}/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(

            @Parameter(
                    description = "Identificativo dell'utente",
                    example = "42",
                    required = true
            )
            @PathVariable Long userId,

            @Parameter(
                    description = "Identificativo della notifica",
                    example = "1001",
                    required = true
            )
            @PathVariable Long notificationId
    ) {
        notificationService.markNotificationAsRead(userId, notificationId);
        return ResponseEntity.noContent().build();
    }

    // ============================
    // DELETE SINGLE NOTIFICATION
    // ============================

    @Operation(
            summary = "Elimina una notifica",
            description = "Rimuove una singola notifica dell'utente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Notifica eliminata con successo"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notifica non trovata"
            )
    })
    @DeleteMapping("/{userId}/{notificationId}")
    public ResponseEntity<Void> deleteNotification(

            @Parameter(
                    description = "Identificativo dell'utente",
                    example = "42",
                    required = true
            )
            @PathVariable Long userId,

            @Parameter(
                    description = "Identificativo della notifica",
                    example = "1001",
                    required = true
            )
            @PathVariable Long notificationId
    ) {
        notificationService.deleteNotification(userId, notificationId);
        return ResponseEntity.noContent().build();
    }

    // ============================
    // DELETE ALL NOTIFICATIONS
    // ============================

    @Operation(
            summary = "Elimina tutte le notifiche di un utente",
            description = "Rimuove tutte le notifiche associate a un utente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Notifiche eliminate con successo"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utente non trovato"
            )
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearNotifications(

            @Parameter(
                    description = "Identificativo dell'utente",
                    example = "42",
                    required = true
            )
            @PathVariable Long userId
    ) {
        notificationService.clearNotificationsByUser(userId);
        return ResponseEntity.noContent().build();
    }
}
