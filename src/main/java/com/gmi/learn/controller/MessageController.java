package com.gmi.learn.controller;

import com.gmi.learn.domain.Messages;
import com.gmi.learn.service.MessageService;
import com.gmi.learn.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.Map;

@Controller
public class MessageController {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @GetMapping(path="/messages")
    public String goToMessages(HttpSession httpSession, Model model){
        return "messages";
    }


    @ResponseBody
    @GetMapping(path="/fetchAllChat")
    public Map<Long, Map<String, Object>> fetchAllChat(HttpSession httpSession, Model model){
        return messageService.getAllUserMessage(httpSession);
    }
    @ResponseBody
    @GetMapping(path="/getMessageBetweenUsers")
    public Map<String , Object> getAllMessagesBetweenUsers(HttpSession httpSession, Model model, @RequestParam("name") String senderName,@RequestParam(value = "messageId", required = false) Long messageId){
        return messageService.getMessageBetweenUsers(httpSession, senderName, messageId);
    }

    @PostMapping(path="/sendMessage")
    @ResponseBody
    public String sendMessage(HttpSession httpSession, @RequestParam("message") String message, @RequestParam("receiver") String receiver){
        messageService.sendMessage(httpSession, message, receiver);
        return "success";
    }

}
