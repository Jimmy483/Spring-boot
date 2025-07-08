package com.gmi.learn.service;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.Messages;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.MessageRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.manager.util.SessionUtils;
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
//        Map<String, Object> groupByEachUserMessage = new HashMap<>();
        List<Long> listOfAccumulatedUniqueSenders = new ArrayList<>();
        Comparator<LocalDateTime> reverseComparator = Comparator.reverseOrder();
        Map<Long, String> senderAndMessageMap = new HashMap<>();

        Map<String, Object> toReturnMap = new HashMap<>();

        Map<LocalDateTime, Map<Long, String>> messageBetweenUsers = new TreeMap<>(reverseComparator);
//        int count =0;

        UserInfo currentUserInfo = userService.getUserInfo(userId);

        List<Messages> messagesList = messageRepository.findAllByReceiverId(userId);
        int count =0;
        for(Messages message: messagesList){
            Map<String, Object> senderDetailMap = new HashMap<>();
            Map<String, Object> messageDetails = new HashMap<>();
            UserInfo userInfo = userService.getUserInfo(message.getSenderId());
            String senderName = userInfo.getUsername();
            String senderPicture = userInfo.getDisplayPicture()!=null?userInfo.getDisplayPicture():defaultUserImage;
            List<Messages> messagesByCurrentUser = messageRepository.findAllByReceiverIdAndSenderId(message.getSenderId(), userId);
            Map<String, Map<String, Object>> editMap = new HashMap<>();
            Object root = toReturnMap.get(message.getSenderId());
            Map<String, Map<String, Object>> getExistingMessages = new HashMap<>();
            Map<String, Object> existingMessageDetail = new HashMap<>();

//            if(!listOfAccumulatedUniqueSenders.contains(message.getSenderId())){
                senderDetailMap.put("senderName", senderName);
                senderDetailMap.put("senderPicture", senderPicture);
//            }


            messageDetails.put("id", message.getId());
            messageDetails.put("senderName",senderName);
            messageDetails.put("lastRead",message.getLastRead());
            messageDetails.put("sentDate",message.getSentDate());
            messageDetails.put("isRead",message.getIsRead());
            messageDetails.put("content",message.getContent());

            if(listOfAccumulatedUniqueSenders.contains(message.getSenderId())){

                if(root instanceof Map<?, ?>){
                    Map<String, Object> firstLevelMap = (Map<String, Object>) root;
//                    Object messageInfo = firstLevelMap.get()
                    for(Map.Entry<String, Object> messageEntry : firstLevelMap.entrySet() ){
                        if(messageEntry.getKey().startsWith(senderName)){
//                            if(messageEntry instanceof Map<?, ?>){
                                existingMessageDetail = (Map<String, Object>)messageEntry;
//                            }e

                            senderDetailMap.put(senderName + existingMessageDetail.get("id"),existingMessageDetail);
                        }
                    }
                }


            }
//            code here to get the exisitng sender if it exists
            senderDetailMap.put(senderName + message.getId(),messageDetails);

            senderAndMessageMap.put(message.getSenderId(),message.getContent());
            messageBetweenUsers.put(message.getSentDate(),senderAndMessageMap);

            if(count==0){
                for(Messages userMessage: messagesByCurrentUser){
                    senderAndMessageMap.put(userId, userMessage.getContent());
                    messageBetweenUsers.put(userMessage.getSentDate(),senderAndMessageMap);
                }
            }


            senderDetailMap.put("ällMessages", messageBetweenUsers);

            if(listOfAccumulatedUniqueSenders.contains(message.getSenderId())){
                editMap.put(message.getSenderId()+"",senderDetailMap);
//                toReturnMap.get(message.getSenderId())
            }
            toReturnMap.put(message.getSenderId() + "",senderDetailMap);

            listOfAccumulatedUniqueSenders.add(message.getSenderId());
            count++;



        }
        System.out.println("all message here " + toReturnMap);
        return  toReturnMap;

//        List<Map<String, Object>> messageList = messageRepository.findAllByReceiverId(userId).stream().map(messages -> {
//
//            UserInfo userInfo = userService.getUserInfo(messages.getSenderId());
//            String senderName = userInfo.getUsername();
//
//            List<Messages> messagesByCurrentUser = messageRepository.findAllByReceiverIdAndSenderId(messages.getSenderId(), userId);
//            String senderPicture = userInfo.getDisplayPicture()!=null?userInfo.getDisplayPicture():defaultUserImage;
//            if(!listOfAccumulatedUniqueSenders.contains(senderName)){
////                groupByEachUserMessage.put(senderName, "");
//                senderDetailMap.put("senderName", senderName);
//                senderDetailMap.put("senderPicture", senderPicture);
//                for(Messages message: messagesByCurrentUser){
//                    senderAndMessageMap.put(message.getSenderId()+message.getSentDate().toString(), message.getContent());
//                    messageBetweenUsers.put(message.getSentDate(),senderAndMessageMap);
//                }
//            }
//
//            Map<String, Object> messageListAll = new HashMap<>();
//
//
//
////            groupByEachUserMessage.put("collectedMessage", "");
//
//
//            messageListAll.put("id", messages.getId());
////            messageListAll.put("senderName", senderName);
//            messageListAll.put("content", messages.getContent());
////            messageListAll.put("senderPicture", senderPicture);
//            messageListAll.put("lastRead", messages.getLastRead());
//            messageListAll.put("isRead", messages.getIsRead());
//            messageListAll.put("sentDate", messages.getSentDate());
//            senderDetailMap.put("messageDetails" + messages.getId(), messageListAll);
//
//            senderAndMessageMap.put(messages.getSenderId()+messages.getSentDate().toString(), messages.getContent());
//            messageBetweenUsers.put(messages.getSentDate(),senderAndMessageMap);
//
////            if(senderDetailMap.containsKey(senderName)){
//////                String message = senderDetailMap.get(senderName) + messages.getContent();
////
////                senderDetailMap.put(senderName, message);
////
////            }
//
//            senderDetailMap.put(senderName, messageBetweenUsers);
//
//
////            groupByEachUserMessage.put(senderName, senderDetailMap);
//
//            listOfAccumulatedUniqueSenders.add(senderName);
//            return  senderDetailMap;
//        }).toList();
//        System.out.println("message list before return " +messageList);
//
//        Map<String, Object> messageMap = new HashMap<>();
//        messageMap.put("allMessage",messageList);
//        return messageMap;
    }
}
