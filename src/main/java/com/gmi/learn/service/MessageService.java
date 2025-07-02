package com.gmi.learn.service;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.Messages;
import com.gmi.learn.repository.MessageRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public void saveMessage(Long receiverId, String content, Boolean isRead, Date lastRead, HttpSession httpSession){
        long senderId= Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        Messages messages = new Messages();
        messages.setReceiverId(receiverId);
        messages.setSenderId(senderId);
        messages.setContent(content);
        messages.setIsRead(isRead);
        messages.setLastRead(lastRead);
        messageRepository.save(messages);
    }
}
