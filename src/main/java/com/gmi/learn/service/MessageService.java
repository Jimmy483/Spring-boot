package com.gmi.learn.service;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.Messages;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.MessageRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.manager.util.SessionUtils;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        messages.setSentDate(LocalDateTime.now());
        messageRepository.save(messages);
    }



    public Map<String, Object> getAllUserMessage(HttpSession httpSession){
        long userId = Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        List<Long> listOfAccumulatedUniqueSenders = new ArrayList<>();
        Comparator<LocalDateTime> reverseComparator = Comparator.reverseOrder();
        Map<String, Object> toReturnMap = new HashMap<>();
        Map<LocalDateTime, Map<Long, String>> messageBetweenUsers = new TreeMap<>(reverseComparator);
        List<Messages> messagesList = messageRepository.findAllByReceiverId(userId);
        int count =0;
        for(Messages message: messagesList){

            Map<Long, String> senderAndMessageMap = new HashMap<>();
            Map<Long, String> senderAndMessageCurrentUserMap = new HashMap<>();
            Map<String, Object> senderDetailMap = new HashMap<>();
            Map<String, Object> messageDetails = new HashMap<>();
            UserInfo userInfo = userService.getUserInfo(message.getSenderId());
            String senderName = userInfo.getUsername();
            String senderPicture = userInfo.getDisplayPicture()!=null?userInfo.getDisplayPicture():defaultUserImage;
            List<Messages> messagesByCurrentUser = messageRepository.findAllByReceiverIdAndSenderId(message.getSenderId(), userId);
            Map<String, Map<String, Object>> editMap = new HashMap<>();
            Object root = toReturnMap.get(message.getSenderId());
            Map<String, Object> existingMessageDetail = new HashMap<>();

            senderDetailMap.put("senderName", senderName);
            senderDetailMap.put("senderPicture", senderPicture);

            messageDetails.put("id", message.getId());
            messageDetails.put("senderName",senderName);
            messageDetails.put("lastRead",message.getLastRead());
            messageDetails.put("sentDate",message.getSentDate());
            messageDetails.put("isRead",message.getIsRead());
            messageDetails.put("content",message.getContent());

            if(listOfAccumulatedUniqueSenders.contains(message.getSenderId())){
                if(root instanceof Map<?, ?>){
                    Map<String, Object> firstLevelMap = (Map<String, Object>) root;
                    for(Map.Entry<String, Object> messageEntry : firstLevelMap.entrySet() ){
                        if(messageEntry.getKey().startsWith(senderName)){
                            existingMessageDetail = (Map<String, Object>)messageEntry;
                            senderDetailMap.put(senderName + existingMessageDetail.get("id"),existingMessageDetail);
                        }
                    }
                }
            }
            senderDetailMap.put(senderName + message.getId(),messageDetails);
            senderAndMessageMap.put(message.getSenderId(),message.getContent());
            messageBetweenUsers.put(message.getSentDate(),senderAndMessageMap);

            if(count==0){
                for(Messages userMessage: messagesByCurrentUser){
                    senderAndMessageCurrentUserMap.put(userId, userMessage.getContent());
                    messageBetweenUsers.put(userMessage.getSentDate(),senderAndMessageCurrentUserMap);
                }
            }
            count++;

            senderDetailMap.put("allMessages", messageBetweenUsers);

            if(listOfAccumulatedUniqueSenders.contains(message.getSenderId())){
                editMap.put(message.getSenderId()+"",senderDetailMap);
            }
            toReturnMap.put(message.getSenderId() + "",senderDetailMap);
            listOfAccumulatedUniqueSenders.add(message.getSenderId());

        }
        System.out.println("all message here " + toReturnMap);
        return  toReturnMap;

    }

    public Map<LocalDateTime, Object> getMessageBetweenUsers(HttpSession httpSession, String senderName){
        System.out.println("sender user name = " + senderName);
        UserInfo userInfo = userService.getUserInfoByUserName(senderName);
        long currentUserId = Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        List<Messages> messagesByCurrentUser = messageRepository.findAllByReceiverIdAndSenderId(userInfo.getId(), currentUserId);
        System.out.println("messages by current  = " + messagesByCurrentUser);
        List<Messages> messageBySender = messageRepository.findAllByReceiverIdAndSenderId(currentUserId, userInfo.getId());
        System.out.println("messages by sender = " + messageBySender);
        Comparator<LocalDateTime> comparator = Comparator.naturalOrder();
        Map<String, String> senderIdWithMessageMapOfSender = new HashMap<>();
        Map<String, String> senderIdWithMessageMapOfCurrentUser = new HashMap<>();
        Map<LocalDateTime, Object> allMessages = new TreeMap<>(comparator);

        for(Messages message: messagesByCurrentUser){
            senderIdWithMessageMapOfCurrentUser.put(message.getSenderId()+"Id" + message.getId(), message.getContent());
            allMessages.put(message.getSentDate(),new HashMap<>(senderIdWithMessageMapOfCurrentUser));
            senderIdWithMessageMapOfCurrentUser.clear();
        }
        System.out.println("all messages before = " + allMessages);
        for(Messages messages: messageBySender){
            senderIdWithMessageMapOfSender.put(messages.getSenderId()+"Id" + messages.getId(), messages.getContent());
            System.out.println("first = " + senderIdWithMessageMapOfSender);
//            allMessages.put(messages.getSentDate(),senderIdWithMessageMapOfSender);
            allMessages.put(messages.getSentDate(),new HashMap<>(senderIdWithMessageMapOfSender));
            senderIdWithMessageMapOfSender.clear();
            System.out.println("last = " + allMessages);
        }
        System.out.println("all messages after = " + allMessages);

        return  allMessages;

    }
}
