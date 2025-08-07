package com.gmi.learn.controller;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @Autowired
    UserService userService;
    @GetMapping(path = "/login")
    public String loginPage(Model model, HttpSession httpSession){
        return "loginPage";
    }

    @GetMapping(path = "/logout")
    public String logoutUser(HttpSession httpSession){
        httpSession.invalidate();
        return "loginPage";
    }

    @ResponseBody
    @GetMapping(path="/loginRequest")
    public Boolean attemptLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession httpSession){
        return userService.checkLoginInfo(username, password, httpSession);
    }
}
