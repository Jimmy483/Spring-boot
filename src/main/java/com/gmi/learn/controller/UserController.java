package com.gmi.learn.controller;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.UserCreateStatusRepository;
import com.gmi.learn.service.UserCreateStatusService;
import com.gmi.learn.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserCreateStatusService userStatus;

    @GetMapping(path="/getAllUserByName")
    @ResponseBody
    public List<Map<String, Object>> getUserByName(@RequestParam("search") String search){
        return userService.getAllUserBySearchName(search);
    }

    @GetMapping(path="/createUserForm")
    public String createUserForm(@RequestParam("request") String request, HttpSession httpSession, Model model){
        if(SessionUtility.getSessionValue(httpSession,"userId")!=null){
            return "dashboard";
        }
        if(userStatus.checkIfRequestExist(request)){
            model.addAttribute("user", new UserInfo());
            model.addAttribute("requestId",request);
            return "createUser";
        }else{
            return "invalidLink";
        }
    }

    @PostMapping(path="/createUser")
    public String createUser(@ModelAttribute UserInfo userinfo, @RequestParam("requestId") String requestId){
        userService.createUser(userinfo);
        userStatus.updateUserStatusRequest(requestId);
        return "dashboard";
    }

}
