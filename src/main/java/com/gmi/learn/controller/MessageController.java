package com.gmi.learn.controller;

import com.gmi.learn.service.MessageService;
import com.gmi.learn.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @ResponseBody
    @PostMapping(path = "/messageToAdmin")
    public String sendMessageToAdmin(@RequestParam("content") String content, HttpSession httpSession){

        long adminId = userService.getAdminInfo().getId();
        messageService.saveMessage(adminId,content, false, null, httpSession);

        return "Message was sent to the Admin";
    }
}
