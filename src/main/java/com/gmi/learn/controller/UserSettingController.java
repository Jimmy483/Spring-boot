package com.gmi.learn.controller;

import com.gmi.learn.dao.UserSettingRepository;
import com.gmi.learn.domain.UserSetting;
import jakarta.servlet.http.HttpSession;
import org.antlr.v4.runtime.misc.ObjectEqualityComparator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/api/setting")
public class UserSettingController {

    private UserSettingRepository userSettingRepository;

    public UserSettingController(UserSettingRepository userSettingRepository){
        this.userSettingRepository=userSettingRepository;
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> getThemeColor(HttpSession httpSession){
        UserSetting userSettings;
        Map<String, Object> returnMap=new HashMap<>();
        long userid= (Long)(SessionUtils.getSessionValue(httpSession,"userId")!=null? SessionUtils.getSessionValue(httpSession, "userId"):0L);
        System.out.println("userid = " + userid);
        if(userid==0){
            returnMap.put("theme","#EAE21D");
        }else {
            userSettings=userSettingRepository.findByUserId(userid);
            returnMap.put("theme",userSettings);
        }

        return returnMap;


    }


}
