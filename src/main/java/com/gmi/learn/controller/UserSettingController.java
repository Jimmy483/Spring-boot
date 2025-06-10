package com.gmi.learn.controller;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.repository.UserSettingRepository;
import com.gmi.learn.domain.UserSetting;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="/api/setting")
public class UserSettingController {

    private UserSettingRepository userSettingRepository;

    public UserSettingController(UserSettingRepository userSettingRepository){
        this.userSettingRepository=userSettingRepository;
    }

//    no use?
    @GetMapping
    @ResponseBody
    public String getThemeColor(HttpSession httpSession){
        UserSetting userSettings;
        Map<String, Object> returnMap=new HashMap<>();
        long userid= (Long)(SessionUtility.getSessionValue(httpSession,"userId")!=null? SessionUtility.getSessionValue(httpSession, "userId"):0L);
        System.out.println("userid = " + userid);
        String theme;
        if(userid==0){
//            returnMap.put("theme","#EAE21D");
            theme="EAE21D";
        }else {
            userSettings=userSettingRepository.findByUserId(userid);
//            returnMap.put("theme",userSettings);
            theme=userSettings.getTheme();
        }

        return theme;


    }


}
