package com.example.db_setup.controller;

//import com.example.db_setup.model.Message;
import com.example.db_setup.model.dto.NewMessageDTO;
import com.example.db_setup.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.db_setup.model.dto.MessageDTO;


import com.example.db_setup.model.Player;
import com.example.db_setup.model.UserProfile;
import com.example.db_setup.model.dto.UserSearchProfileDTO;
import com.example.db_setup.model.repository.UserProfileRepository;
import com.example.db_setup.service.PlayerService;
import com.example.db_setup.service.UserSocialService;
import com.example.db_setup.service.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "http://localhost:8085")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/new")
    public ResponseEntity<Void> createMessage(@RequestBody NewMessageDTO dto) {

        logger.info("T23 /messages/new dto: senderId={}, receiverId={}, content='{}'",
                dto.getSenderId(),
                dto.getReceiverId(),
                dto.getContent());

        if (dto.getSenderId() == null || dto.getReceiverId() == null ||
                dto.getContent() == null || dto.getContent().isBlank()) {

            logger.warn("T23 /messages/new BAD_REQUEST: senderId={}, receiverId={}, content='{}'",
                    dto.getSenderId(),
                    dto.getReceiverId(),
                    dto.getContent());

            return ResponseEntity.badRequest().build();
        }

        messageService.createMessage(dto.getSenderId(), dto.getReceiverId(), dto.getContent());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/inbox/{userId}")
    public ResponseEntity<List<MessageDTO>> inbox(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getInbox(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long id,
            @RequestParam Long userId) {
        messageService.deleteMessage(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/outbox/{userId}")
    public ResponseEntity<List<MessageDTO>> outbox(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getOutbox(userId));
    }


}


