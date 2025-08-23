package com.gmi.learn.controller;

import com.gmi.learn.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;


@Controller
public class MainController {


    File file=new File("src\\main\\resources\\templates\\dashboard.html");


    @Autowired
    private UserService userService;


    @RequestMapping(path = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String dashboard(Model model, HttpSession httpSession) {
        model.addAttribute("name", httpSession.getAttribute("username"));
        model.addAttribute("loggedIn",httpSession.getAttribute("username")!=null);
        return "dashboard";
    }


    @GetMapping(path = "/profile")
    public String goToProfile(HttpSession httpSession, Model model){
        String templateToRender="profile.html";
        Map<String, Object> retMap = userService.getUserInfoMap(httpSession);
        model.addAttribute("data",retMap);
        model.addAttribute("templateToRender",templateToRender);
        return "profileGeneric";
    }

    @GetMapping(path="/error")
    public String errorPage(){
        return "error";
    }


}
