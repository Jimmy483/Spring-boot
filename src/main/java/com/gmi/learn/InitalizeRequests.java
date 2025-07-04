package com.gmi.learn;
import com.gmi.learn.repository.UserSettingRepository;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.domain.UserSetting;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
        SessionUtil.storeSessionValue(sessionAttributes,"username",userInfo.get(0).getUsername());
        SessionUtil.storeSessionValue(sessionAttributes,"userId",userInfo.get(0).getId());
        SessionUtil.storeSessionValue(sessionAttributes,"themeColour",getUserThemeColor(userInfo.get(0).getId()));
//        SessionUtil.storeSessionValue(sessionAttributes,"userRole",getUserThemeColor(userInfo.get(0).getId()));
    }

    @GetMapping("/get-session")
    public Boolean getSessionAttributes(HttpSession sessionAttributes) {
        String username = (String) SessionUtil.getSessionValue(sessionAttributes, "username");
        Long userId = (Long) SessionUtil.getSessionValue(sessionAttributes, "userId");

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
            toReturn="FFFFF";
        }else{
            userSetting=userSettingRepository.findByUserId(userId);
            if(userSetting!=null){
                toReturn=userSetting.getTheme()!=null?userSetting.getTheme():"FFA07A";

            }else{
                toReturn="FFA07A";
            }
        }
        System.out.println("theme colour = " + toReturn);
        return toReturn;

    }

}

class SessionUtil {

    // Utility method to store a key-value pair in the session
    public static void storeSessionValue(HttpSession session, String key, Object value) {
        session.setAttribute(key, value);
    }

    // Utility method to retrieve a value from the session
    public static Object getSessionValue(HttpSession session, String key) {
        return session.getAttribute(key);
    }
}