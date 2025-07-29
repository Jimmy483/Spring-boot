package com.gmi.learn.controller;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.model.Theme;
import com.gmi.learn.service.SettingService;
import com.gmi.learn.service.UserCreateStatusService;
import com.gmi.learn.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
//Dedicated to setting page
@Controller
public class SettingsController {


    @Autowired
    SettingService settingService;


    @Autowired
    UserCreateStatusService statusService;

    private final String DEFAULT_THEME = "EAE21D";

    @GetMapping(path = "/loadSetting")
    public String loadSettingsForm(Model model, HttpSession session){
        model.addAttribute("themeColours",getThemeColours());
        model.addAttribute("themeForm",new Theme(SessionUtility.getSessionValue(session,"themeColour")!=null? SessionUtility.getSessionValue(session,"themeColour").toString():"EAE21D"));
        System.out.println("theme = " + SessionUtility.getSessionValue(session,"themeColour"));
        return "settings";

    }

    @GetMapping(path="/createUserLInk")
    public String loadRequestForm(Model model, HttpSession httpSession){
        String templateToRender="createUserRequest";
        List<Map<String, Object>> returnMap = settingService.getAllUserRequestsUrl();
        model.addAttribute("data", returnMap);
        model.addAttribute("templateToRender", templateToRender);
        return "profileGeneric";
    }

    @GetMapping(path="/createLink")
    public Boolean createLinkForNewUser(HttpSession httpSession){
        return statusService.createStatusUrl();
    }


    public Map<String, String> getThemeColours(){
        Map<String,String> themeColours = new HashMap<>();
        themeColours.put("Sky Blue","42c5f5");
        themeColours.put("Light Gray", "E5E5E5");
        themeColours.put("Ivory","FFFFF0");
        themeColours.put("Beige","F8F8F2");
        themeColours.put("Burnt Orange","FFA07A");
        themeColours.put("Teal","00CED1");
        themeColours.put("Pale Blue","DCEEFF");
        themeColours.put("Soft Mint","DFFFE0");
        themeColours.put("Pale Peach","FFE5D9");

        return themeColours;
    }

    @PostMapping(path = "/updateSetting")
    public String applySetting(Model model, HttpSession session, @ModelAttribute Theme theme){
        System.out.println("success");
        String themeColour= theme.getColours();
        System.out.println("Theme = " + themeColour);
//        System.out.println("Theme = " + theme);
        if(!getThemeColours().containsValue(themeColour))
            themeColour=DEFAULT_THEME;
        settingService.UpdateSetting(session,themeColour);
        model.addAttribute("themeColours",getThemeColours());
        model.addAttribute("themeForm",new Theme(SessionUtility.getSessionValue(session,"themeColour")!=null?SessionUtility.getSessionValue(session,"themeColour").toString():"EAE21D"));
        return "settings";
    }
}
