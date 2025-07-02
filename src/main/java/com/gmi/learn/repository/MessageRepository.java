package com.gmi.learn.repository;

import com.gmi.learn.domain.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Messages, Long> {

}
