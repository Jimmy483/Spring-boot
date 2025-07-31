package com.gmi.learn.controller;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.service.UserCreateStatusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserCreateStatusController {

    @Autowired
    private UserCreateStatusService userStatus;

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

    @ResponseBody
    @PostMapping(path="/revokeRequestStatus")
    public String revokeRequest(HttpSession httpSession, @RequestParam("requestId") String requestId){
        if(userStatus.revokeRequest(requestId)){
            return "Success";
        }else{
            return "Fail";
        }
    }
}
