package com.gmi.learn.repository;

import com.gmi.learn.domain.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Messages, Long> {

    List<Messages> findAllByReceiverId(long id);

    List<Messages> findAllByReceiverIdAndSenderId(long receiverId, long senderId);

    List<Messages> findAllByReceiverIdAndSenderIdAndIdGreaterThan(long receiverId, long senderId, long messageId);
}
