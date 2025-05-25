package com.gmi.learn.controller;
import com.gmi.learn.dao.UserSettingRepository;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.domain.UserSetting;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("username")
public class InitalizeRequests{
    UserSettingRepository userSettingRepository;

    public InitalizeRequests(UserSettingRepository userSettingRepository){
        this.userSettingRepository=userSettingRepository;
    }

    @GetMapping("/set-session")
    public void setSessionAttributes(HttpSession sessionAttributes, List<UserInfo> userInfo){
//        model.addAttribute("username","Jimmy");
        System.out.println("test this");
        SessionUtils.storeSessionValue(sessionAttributes,"username",userInfo.get(0).getUsername());
        SessionUtils.storeSessionValue(sessionAttributes,"userId",userInfo.get(0).getId());
        SessionUtils.storeSessionValue(sessionAttributes,"themeColour",getUserThemeColor(userInfo.get(0).getId()));
    }

    @GetMapping("/get-session")
    public Boolean getSessionAttributes(HttpSession sessionAttributes) {
        String username = (String) SessionUtils.getSessionValue(sessionAttributes, "username");
        Long userId = (Long) SessionUtils.getSessionValue(sessionAttributes, "userId");

        System.out.println("username = " + username);
        System.out.println("userId = " + userId);
        if(!username.isEmpty() && userId!=null){
            return true;
        }else{
            return false;
        }

    }

    public String getUserThemeColor(Long userId){
//        Long userid
        UserSetting userSetting;
        System.out.println("userid = " + userId);
        String toReturn;
        if(userId==0){
            toReturn="#FFFFF";
        }else{
            userSetting=userSettingRepository.findByUserId(userId);
            toReturn=userSetting.getTheme();
        }
        System.out.println("theme colour = " + toReturn);
        return toReturn;

    }

}

class SessionUtils {

    // Utility method to store a key-value pair in the session
    public static void storeSessionValue(HttpSession session, String key, Object value) {
        session.setAttribute(key, value);
    }

    // Utility method to retrieve a value from the session
    public static Object getSessionValue(HttpSession session, String key) {
        return session.getAttribute(key);
    }
}