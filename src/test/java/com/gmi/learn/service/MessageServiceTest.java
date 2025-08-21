package com.gmi.learn.service;

import com.gmi.learn.InitializeRequests;
import com.gmi.learn.domain.Messages;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.MessageRepository;
import com.gmi.learn.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;


//@DataJpaTest
@SpringBootTest
@Import({UserService.class, UserCreateStatusService.class, UserRoleService.class, BCryptPasswordEncoder.class, InitializeRequests.class, MessageService.class})
class MessageServiceTest {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserService userService;

    @Test
    void checkJPT(){
        assertTrue(true);
    }


    @Test
    void checkMapKeysOfAllUsers(){
        UserInfo currentUser = new UserInfo();
        currentUser.setUsername("Gmi");
        currentUser.setPasswd("GMi");
        currentUser.setFirstName("jimmy");
        currentUser.setLastName("Sherpa");
        currentUser.setDisplayPicture(null);
        Long currentId = userInfoRepository.save(currentUser).getId();

        UserInfo relatedUser = new UserInfo();
        relatedUser.setUsername("Musk");
        relatedUser.setPasswd("Musl");
        Long otherUserId = userInfoRepository.save(relatedUser).getId();

        Messages sent = new Messages();
        sent.setReceiverId(otherUserId);
        sent.setSentDate(LocalDateTime.of(2025, 8, 10, 12, 30));
        sent.setIsRead(false);
        sent.setSenderId(currentId);
        sent.setContent("Hey Admin");
        messageRepository.save(sent);

        Messages received = new Messages();
        received.setReceiverId(currentId);
        received.setSenderId(otherUserId);
        received.setContent("Hey wassup");
        received.setIsRead(false);
        received.setSentDate(LocalDateTime.now());
        messageRepository.save(received);

        MockHttpSession httpSession = new MockHttpSession();
        httpSession.setAttribute("userId", currentId);



        Map<Long, Map<String, Object>> map = messageService.getAllUserMessage(httpSession);
        Map<String, Object> firstEntry = map.entrySet().iterator().next().getValue();
        assertTrue(firstEntry.containsKey("senderName"));
        assertTrue(firstEntry.containsKey("allMessages"));
    }

    @Test
    void checkMapKeysOfAllTheMessagesBetweenUsers(){
        UserInfo currentUser = new UserInfo();
        currentUser.setUsername("Gmi");
        currentUser.setPasswd("GMi");
        currentUser.setFirstName("jimmy");
        currentUser.setLastName("Sherpa");
        currentUser.setDisplayPicture(null);
        long currentId = userInfoRepository.save(currentUser).getId();

        UserInfo relatedUser = new UserInfo();
        relatedUser.setUsername("Musk");
        relatedUser.setPasswd("Musl");
        long otherUserId = userInfoRepository.save(relatedUser).getId();

        long maxId = currentId > otherUserId ? currentId: otherUserId;

        Messages sent = new Messages();
        sent.setReceiverId(otherUserId);
        sent.setSentDate(LocalDateTime.of(2025, 8, 10, 12, 30));
        sent.setIsRead(false);
        sent.setSenderId(currentId);
        sent.setContent("Hey Admin");
        messageRepository.save(sent);

        Messages received = new Messages();
        received.setReceiverId(currentId);
        received.setSenderId(otherUserId);
        received.setContent("Hey wassup");
        received.setIsRead(false);
        received.setSentDate(LocalDateTime.now());
        messageRepository.save(received);

        MockHttpSession httpSession = new MockHttpSession();
        httpSession.setAttribute("userId", currentId);

        Map<String, Object> whenMessageNullMap = messageService.getMessageBetweenUsers(httpSession, "Musk", null);
        assertTrue(whenMessageNullMap.containsKey("row"));
        assertTrue(whenMessageNullMap.containsKey("maxId"));

        Map<String, Object> whenMessageIsNotNullMap = messageService.getMessageBetweenUsers(httpSession, "Musk", maxId);
        assertTrue(whenMessageIsNotNullMap.containsKey("row"));
        assertTrue(whenMessageIsNotNullMap.containsKey("maxId"));

        Map<LocalDateTime, TreeMap> allMessage= new HashMap<>();
        if(whenMessageIsNotNullMap.get("row") instanceof Map<?, ?>){
            allMessage = (Map<LocalDateTime, TreeMap>) whenMessageIsNotNullMap.get("row");
        }
        assertTrue(allMessage.isEmpty());
    }

}