package com.gmi.learn.controller;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.domain.UserRole;
import com.gmi.learn.repository.UserCreateStatusRepository;
import com.gmi.learn.service.UserCreateStatusService;
import com.gmi.learn.service.UserRoleService;
import com.gmi.learn.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserCreateStatusService userStatus;

    @Autowired
    UserRoleService userRoleService;

    @GetMapping(path="/getAllUserByName")
    @ResponseBody
    public List<Map<String, Object>> getUserByName(@RequestParam("search") String search){
        return userService.getAllUserBySearchName(search);
    }


    @PostMapping(path="/createUser")
    public String createUser(@ModelAttribute UserInfo userinfo, @RequestParam("requestId") String requestId){
        userService.createUser(userinfo);
        userRoleService.changeUserRole(userService.getUserInfoByUserName(userinfo.getUsername()), "Assistant");
        userStatus.updateUserStatusRequest(requestId);
        return "redirect:/login";
    }

    @GetMapping(path="/roleAssign")
    public String roleAssign(HttpSession httpSession, Model model){
        Map<String, Object> retMap = userService.getAllUserMap();
        model.addAttribute("row",retMap);
        model.addAttribute("templateToRender", "assignRole");
        return "profileGeneric";
    }

    @ResponseBody
    @PostMapping(path="/updateUser")
    public Boolean updateUser(HttpSession httpSession, @RequestParam("fName") String firstName, @RequestParam("lName") String lName, @RequestParam(value = "image",required = false)  MultipartFile file){
        return userService.updateUserInfo(httpSession, firstName, lName, file);
    }

    @ResponseBody
    @PostMapping(path="/applyRoleForUser")
    public String applyRoleForUser(HttpSession httpSession, @RequestParam("role") String role, @RequestParam("userId") Long userId){
        userService.updateUserRole(role, userId);
        return "Success";
    }


    @ResponseBody
    @GetMapping(path="/checkPassword")
    public Boolean checkPassword(HttpSession httpSession, @RequestParam("password") String password){
        System.out.println("check checkPass");
        return userService.checkPassword(httpSession, password);
    }

    @PostMapping(path="/changePassword")
    public String changePassword(HttpSession httpSession, Model model, @ModelAttribute("userInfo") UserInfo userInfo){
        userService.changePassword(userInfo);
        return "redirect:/profile";
    }

    @GetMapping(path="/goToPasswordForm")
    public String goToPasswordForm(HttpSession httpSession, Model model){
        String templateToRender="changePassword";
        model.addAttribute("templateToRender", templateToRender);
        model.addAttribute("userInfo", userService.getUserInfo(Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString())));
        return "profileGeneric";
    }


    @ResponseBody
    @GetMapping(path="/checkForExistingUsername")
    public Boolean checkIfUserExists(HttpSession httpSession, @RequestParam("username") String username){
        return userService.checkIfUserExists(username);
    }
}
