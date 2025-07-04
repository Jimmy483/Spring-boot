package com.gmi.learn.repository;

import com.gmi.learn.domain.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Messages, Long> {

    List<Messages> findAllByReceiverId(long id);
}
