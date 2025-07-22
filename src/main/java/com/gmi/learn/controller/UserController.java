package com.gmi.learn.controller;

import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path="/getAllUserByName")
    @ResponseBody
    public List<Map<String, Object>> getUserByName(@RequestParam("search") String search){
        return userService.getAllUserBySearchName(search);
    }
}
