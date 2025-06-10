package com.gmi.learn.controller;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.model.Theme;
import com.gmi.learn.service.SettingService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
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

    @GetMapping(path = "/loadSetting")
    public String loadSettingsForm(Model model, HttpSession session){
        model.addAttribute("themeColours",getThemeColours());
        model.addAttribute("themeForm",new Theme(SessionUtility.getSessionValue(session,"themeColour")!=null? SessionUtility.getSessionValue(session,"themeColour").toString():"EAE21D"));
        System.out.println("theme = " + SessionUtility.getSessionValue(session,"themeColour"));
        return "settings";

    }

    public Map<String, String> getThemeColours(){
        Map<String,String> themeColours = new HashMap<>();
        themeColours.put("Sky Blue","42c5f5");
        themeColours.put("Light Gray", "F2F2F2");
        themeColours.put("Ivory","FFFFF0");
        themeColours.put("Ghost White","F8F8FF");

        return themeColours;
    }

    @PostMapping(path = "/updateSetting")
    public String applySetting(Model model, HttpSession session, @ModelAttribute Theme theme){
        System.out.println("success");
        String themeColour= theme.getColours();
        System.out.println("Theme = " + themeColour);
//        System.out.println("Theme = " + theme);
        settingService.UpdateSetting(session,themeColour);
        model.addAttribute("themeColours",getThemeColours());
        model.addAttribute("themeForm",new Theme(SessionUtility.getSessionValue(session,"themeColour")!=null?SessionUtility.getSessionValue(session,"themeColour").toString():"EAE21D"));
        return "settings";
    }
}
