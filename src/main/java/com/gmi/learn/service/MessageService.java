package com.gmi.learn.service;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.Messages;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.MessageRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.manager.util.SessionUtils;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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



    public Map<Long, Object> getAllUserMessage(HttpSession httpSession){
        long userId = Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        List<Long> listOfAccumulatedUniqueSenders = new ArrayList<>();
//        Comparator<LocalDateTime> reverseComparator = Comparator.reverseOrder();
        Map<Long, Object> toReturnMap = new HashMap<>();
        Map<LocalDateTime, Map<Long, String>> messageBetweenUsers = new TreeMap<>();
        List<Messages> messagesList = messageRepository.findAllByReceiverId(userId);
//        int count =0;
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
            System.out.println("to return map = " + toReturnMap);
            System.out.println("sender id = " + message.getSenderId());
            System.out.println("sender id = " + message.getSenderId());
            Object root = toReturnMap.get(message.getSenderId());
            Map<String, Object> existingMessageDetail = new HashMap<>();

            senderDetailMap.put("senderName", senderName);
            senderDetailMap.put("senderPicture", senderPicture);
            senderDetailMap.put("senderId", message.getSenderId());

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
                            System.out.println("message entry = " + messageEntry);
                            existingMessageDetail = (Map<String, Object>)messageEntry.getValue();
                            senderDetailMap.put(senderName + existingMessageDetail.get("id"),existingMessageDetail);
                        }
                    }
                }
            }
            senderDetailMap.put(senderName + message.getId(),messageDetails);
            System.out.println("content message = " + message.getContent());
            senderAndMessageMap.put(message.getSenderId(),message.getContent());
            messageBetweenUsers.put(message.getSentDate(),new HashMap<>(senderAndMessageMap));

            System.out.println("list of accum = " + listOfAccumulatedUniqueSenders);
            System.out.println("message sender id = " + message.getSenderId());
            if(listOfAccumulatedUniqueSenders.contains(message.getSenderId())){
                System.out.println("first level = " + root);
                System.out.println("first level class = " + root.getClass());
                if(root instanceof Map<?, ?>){
                    System.out.println("if root level");
                    Map<String, Object> senderMap = (Map<String, Object>) root;
                    Map<LocalDateTime, Map<Long, String>> allMMap = (Map<LocalDateTime, Map<Long, String>>) senderMap.get("allMessages");
                    System.out.println("all Mmap = " + allMMap);
                    for(Map.Entry<LocalDateTime, Map<Long, String>> entryAllMap :allMMap.entrySet()){
                        messageBetweenUsers.put(entryAllMap.getKey(),new HashMap<>(entryAllMap.getValue()));
                    }
                }
            }

//            for collecting all the message from the current user sent to the related user
            if(!listOfAccumulatedUniqueSenders.contains(message.getSenderId())){
                System.out.println("inside the current user message loop");
                for(Messages userMessage: messagesByCurrentUser){
                    senderAndMessageCurrentUserMap.put(userId, userMessage.getContent());
                    messageBetweenUsers.put(userMessage.getSentDate(),new HashMap<>(senderAndMessageCurrentUserMap));
                }
            }
//            count++;
            System.out.println("message  betn after = " + messageBetweenUsers);


            // issue here? appending the same message for all users?
            senderDetailMap.put("allMessages",new TreeMap<>(messageBetweenUsers));
            messageBetweenUsers.clear();
            System.out.println("sender detail map = " + senderDetailMap);

            if(listOfAccumulatedUniqueSenders.contains(message.getSenderId())){
                editMap.put(message.getSenderId()+"",senderDetailMap);
            }
            toReturnMap.put(message.getSenderId(),senderDetailMap);

            if(!listOfAccumulatedUniqueSenders.contains(message.getSenderId())){
                listOfAccumulatedUniqueSenders.add(message.getSenderId());
            }


        }
        System.out.println("all message here " + toReturnMap);
        return  toReturnMap;

    }

    public Map<String, Object> getMessageBetweenUsers(HttpSession httpSession, String senderName, Long messageId){
        System.out.println("sender user name = " + senderName);
        System.out.println("messageId = " + messageId);
        long maxId=0L;
        UserInfo userInfo = userService.getUserInfoByUserName(senderName);
        List<Messages> messageBySender=new ArrayList<>();
        List<Messages> messagesByCurrentUser = new ArrayList<>();
        long currentUserId = Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        if(messageId!=null){
            messageBySender = messageRepository.findAllByReceiverIdAndSenderIdAndIdGreaterThan(currentUserId, userInfo.getId(), messageId);
        }else{
            messagesByCurrentUser = messageRepository.findAllByReceiverIdAndSenderId(userInfo.getId(), currentUserId);
            messageBySender = messageRepository.findAllByReceiverIdAndSenderId(currentUserId, userInfo.getId());
        }

        System.out.println("messages by current  = " + messagesByCurrentUser);

        System.out.println("messages by sender = " + messageBySender);
        Comparator<LocalDateTime> comparator = Comparator.naturalOrder();
        Map<String, Object> senderIdWithMessageMapOfSender = new HashMap<>();
        Map<String, Object> senderIdWithMessageMapOfCurrentUser = new HashMap<>();
        Map<LocalDateTime, Object> allMessages = new TreeMap<>(comparator);

        TreeMap<LocalDateTime, String> messageWithOrderMapForCurrentUser = new TreeMap<>();
        if(messageId==null){
            for(Messages message: messagesByCurrentUser){
                messageWithOrderMapForCurrentUser.put(message.getSentDate(), message.getContent());
                senderIdWithMessageMapOfCurrentUser.put(message.getSenderId()+"Id" + message.getId(),new TreeMap<>(messageWithOrderMapForCurrentUser));
                allMessages.put(message.getSentDate(),new HashMap<>(senderIdWithMessageMapOfCurrentUser));
                senderIdWithMessageMapOfCurrentUser.clear();
                if(message.getId()>maxId){
                    maxId=message.getId();
                }
                messageWithOrderMapForCurrentUser.clear();
            }
        }

        System.out.println("all messages before = " + allMessages);
        TreeMap<LocalDateTime, String> messageWithOrderMap = new TreeMap<>();
        for(Messages messages: messageBySender){
            messageWithOrderMap.put(messages.getSentDate(), messages.getContent());
            senderIdWithMessageMapOfSender.put(messages.getSenderId()+"Id" + messages.getId(),new TreeMap<>(messageWithOrderMap));
            System.out.println("first = " + senderIdWithMessageMapOfSender);
//            allMessages.put(messages.getSentDate(),senderIdWithMessageMapOfSender);
            allMessages.put(messages.getSentDate(),new HashMap<>(senderIdWithMessageMapOfSender));
            senderIdWithMessageMapOfSender.clear();
            if(messages.getId()>maxId){
                maxId=messages.getId();
            }
            messageWithOrderMap.clear();
            System.out.println("last = " + allMessages);
        }

        if(messageId!=null){
            if(maxId==0){
                maxId= messageId;
            }
        }
        System.out.println("all messages after = " + allMessages);
        System.out.println("maxId = " + maxId);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("row",allMessages);
        returnMap.put("maxId", maxId);

        return  returnMap;

    }

    public void sendMessage(HttpSession httpSession, String message, String receiver){
        long receiverId = userService.getUserInfoByUserName(receiver).getId();
        long currentUserId = Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        Messages messages = new Messages();
        messages.setSenderId(currentUserId);
        messages.setReceiverId(receiverId);
        messages.setContent(message);
        messages.setLastRead(null);
        messages.setIsRead(false);
        messages.setSentDate(LocalDateTime.now());
        messageRepository.save(messages);
    }
}
