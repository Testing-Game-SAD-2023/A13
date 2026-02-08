package com.example.db_setup.service;

import com.example.db_setup.model.Message;
import com.example.db_setup.model.UserProfile;
import com.example.db_setup.model.dto.MessageDTO;
import com.example.db_setup.model.repository.MessageRepository;
import com.example.db_setup.model.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserProfileRepository userProfileRepository;

    public MessageService(MessageRepository messageRepository,
                          UserProfileRepository userProfileRepository) {
        this.messageRepository = messageRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public void createMessage(Long senderId, Long receiverId, String content) {
        Message msg = new Message();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setContent(content);
        msg.setTimestamp(LocalDateTime.now());
        messageRepository.save(msg);
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> getInbox(Long userId) {
        List<Message> raw = messageRepository.findByReceiverIdOrderByTimestampDesc(userId);
        List<MessageDTO> dtos = new ArrayList<>();

        for (Message msg : raw) {
            MessageDTO dto = new MessageDTO();
            dto.setId(msg.getId());
            dto.setSenderId(msg.getSenderId());
            dto.setReceiverId(msg.getReceiverId());
            dto.setContent(msg.getContent());
            dto.setTimestamp(msg.getTimestamp());

            if (msg.getSenderId() != null) {
                UserProfile senderProfile = userProfileRepository.findByPlayer_ID(msg.getSenderId());
                if (senderProfile != null) {
                    dto.setSenderEmail(senderProfile.getEmail());
                }
            }

            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    public void deleteMessage(Long messageId, Long currentUserId) {
        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));


        boolean isSender = currentUserId.equals(msg.getSenderId());
        boolean isReceiver = currentUserId.equals(msg.getReceiverId());

        if (!isSender && !isReceiver) {
            // Se l'utente non è né chi ha inviato né chi ha ricevuto, blocchiamo l'azione
            throw new RuntimeException("Not authorized to delete this message");
        }

        messageRepository.delete(msg);
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> getOutbox(Long userId) {
        List<Message> raw = messageRepository.findBySenderIdOrderByTimestampDesc(userId);
        List<MessageDTO> dtos = new ArrayList<>();

        for (Message msg : raw) {
            MessageDTO dto = new MessageDTO();
            dto.setId(msg.getId());
            dto.setSenderId(msg.getSenderId());
            dto.setReceiverId(msg.getReceiverId());
            dto.setContent(msg.getContent());
            dto.setTimestamp(msg.getTimestamp());

            // Mostra email del DESTINATARIO nell'outbox
            if (msg.getReceiverId() != null) {
                UserProfile receiverProfile = userProfileRepository.findByPlayer_ID(msg.getReceiverId());
                if (receiverProfile != null) {
                    dto.setReceiverEmail(receiverProfile.getEmail());
                }
            }

            dtos.add(dto);
        }

        return dtos;
    }


}
