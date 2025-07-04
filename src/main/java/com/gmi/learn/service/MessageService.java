package com.gmi.learn.service;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.Messages;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.MessageRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final String defaultUserImage="/profilePics/whoAmI.jpg";

    @Autowired
    private UserService userService;

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

    public Map<String, Object> getAllUserMessage(HttpSession httpSession){
        long userId = Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());

        List<Map<String, Object>> messageList = messageRepository.findAllByReceiverId(userId).stream().map(messages -> {
            Map<String, Object> messageListAll = new HashMap<>();
            UserInfo userInfo = userService.getUserInfo(messages.getSenderId());
            String senderName = userInfo.getUsername();
            String senderPicture = userInfo.getDisplayPicture()!=null?userInfo.getDisplayPicture():defaultUserImage;
            messageListAll.put("id", messages.getId());
            messageListAll.put("senderName", senderName);
            messageListAll.put("content", messages.getContent());
            messageListAll.put("senderPicture", senderPicture);
            messageListAll.put("lastRead", messages.getLastRead());
            messageListAll.put("isRead", messages.getIsRead());
            return  messageListAll;
        }).toList();
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("allMessage",messageList);
        return messageMap;
    }
}
