package com.example.db_setup.model.repository;

import com.example.db_setup.model.Message;

import com.example.db_setup.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // inbox di un utente
    List<Message> findByReceiverIdOrderByTimestampDesc(Long receiverId);

    // messaggi inviati da un utente (outbox)
    List<Message> findBySenderIdOrderByTimestampDesc(Long senderId);


}
