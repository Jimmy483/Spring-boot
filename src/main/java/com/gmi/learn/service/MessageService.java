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
    private final String defaultUserImage="/profilePics/defaultDP.jpg";

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



    public Map<Long, Map<String, Object>> getAllUserMessage(HttpSession httpSession){
        long userId = Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        List<Long> listOfAccumulatedUniqueSenders = new ArrayList<>();
        Map<Long, Map<String, Object>> toReturnMap = new HashMap<>();
        Map<LocalDateTime, String> messageBetweenUsers = new TreeMap<>();
        List<Messages> received = messageRepository.findAllByReceiverId(userId);
        List<Messages> sent = messageRepository.findAllBySenderId(userId);

        List<Messages> allMessages = new ArrayList<>();
        allMessages.addAll(received);
        allMessages.addAll(sent);
        int loopCount = 0;


        Map<Long, LocalDateTime> userLastDateMap = new HashMap<>();
        for(Messages message: allMessages){
            loopCount++;

            long otherId = message.getSenderId()==userId?message.getReceiverId():message.getSenderId();

            Map<String, Object> senderDetailMap = new HashMap<>();
            UserInfo userInfo = userService.getUserInfo(otherId);
            String senderName = userInfo.getUsername();
            String senderPicture = userInfo.getDisplayPicture()!=null?userInfo.getDisplayPicture():defaultUserImage;

            Object root = toReturnMap.get(otherId);

            if(listOfAccumulatedUniqueSenders.contains(otherId)){
                if(userLastDateMap.get(otherId).isBefore(message.getSentDate())){
                    senderDetailMap.put("lastMessageDate",message.getSentDate());
                    userLastDateMap.put(message.getSenderId(),message.getSentDate());
                }else if(senderDetailMap.isEmpty()){
                    senderDetailMap.put("lastMessageDate", userLastDateMap.get(otherId));
                }
            }else{
                senderDetailMap.put("lastMessageDate", message.getSentDate());
                userLastDateMap.put(otherId,message.getSentDate());
            }

            senderDetailMap.put("senderName", senderName);
            senderDetailMap.put("senderPicture", senderPicture);
            senderDetailMap.put("senderId", otherId);

            if(listOfAccumulatedUniqueSenders.contains(otherId)){
                if(root instanceof Map<?, ?>){
                    Map<String, Object> senderMap = (Map<String, Object>) root;
                    Map<LocalDateTime, String> allMMap = (Map<LocalDateTime, String>) senderMap.get("allMessages");
                    for(Map.Entry<LocalDateTime, String> entryAllMap :allMMap.entrySet()){
                        if(entryAllMap.getKey().isBefore(message.getSentDate())){
                            messageBetweenUsers.put(message.getSentDate(), message.getContent());
                        }else{
                            messageBetweenUsers.put(entryAllMap.getKey(),entryAllMap.getValue());
                        }
                        senderDetailMap.put("allMessages", new HashMap<>(messageBetweenUsers));
                    }
                }
            }else{
                messageBetweenUsers.put(message.getSentDate(), message.getContent());
                senderDetailMap.put("allMessages", new HashMap<>(messageBetweenUsers));
            }



            messageBetweenUsers.clear();


            toReturnMap.put(otherId,senderDetailMap);

            if(!listOfAccumulatedUniqueSenders.contains(otherId)){
                listOfAccumulatedUniqueSenders.add(otherId);
            }


        }

        List<Map.Entry<Long, Map<String, Object>>> sortList = new ArrayList<>(toReturnMap.entrySet());
        sortList.sort(Comparator.comparing((Map.Entry<Long, Map<String, Object>>entry)->{
            Map<String, Object> sendMap= entry.getValue();
            return (sendMap.get("lastMessageDate") instanceof  LocalDateTime)? (LocalDateTime) sendMap.get("lastMessageDate"):LocalDateTime.MIN;
             }
        ).reversed());

//        need to study this
        Map<Long, Map<String, Object>> newReturnMap = sortList.stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a,b) ->a,
                LinkedHashMap::new
        ));

        return  newReturnMap;

    }

    public Map<String, Object> getMessageBetweenUsers(HttpSession httpSession, String senderName, Long messageId){
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

        TreeMap<LocalDateTime, String> messageWithOrderMap = new TreeMap<>();
        for(Messages messages: messageBySender){
            messageWithOrderMap.put(messages.getSentDate(), messages.getContent());
            senderIdWithMessageMapOfSender.put(messages.getSenderId()+"Id" + messages.getId(),new TreeMap<>(messageWithOrderMap));
            allMessages.put(messages.getSentDate(),new HashMap<>(senderIdWithMessageMapOfSender));
            senderIdWithMessageMapOfSender.clear();
            if(messages.getId()>maxId){
                maxId=messages.getId();
            }
            messageWithOrderMap.clear();
        }

        if(messageId!=null){
            if(maxId==0){
                maxId= messageId;
            }
        }
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
