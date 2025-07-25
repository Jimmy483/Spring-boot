package com.gmi.learn.controller;

import com.gmi.learn.SessionUtility;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping(path = "/login")
    public String loginPage(Model model, HttpSession httpSession){

        System.out.println("userid login = " + SessionUtility.getSessionValue(httpSession,"userId"));
        if(SessionUtility.getSessionValue(httpSession,"userId")!=null){
            model.addAttribute("name", httpSession.getAttribute("username"));  // You can pass more model attributes if necessary
            return "dashboard";

        }


        return "loginPage";
    }

    @GetMapping(path = "/logout")
    public String logoutUser(HttpSession httpSession){
        httpSession.invalidate();
        return "loginPage";
    }
}
