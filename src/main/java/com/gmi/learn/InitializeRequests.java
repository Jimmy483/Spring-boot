package com.gmi.learn;
import com.gmi.learn.repository.UserSettingRepository;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.domain.UserSetting;
import com.gmi.learn.service.UserRoleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("username")
public class InitializeRequests {
    UserSettingRepository userSettingRepository;

    @Autowired
    UserRoleService userRoleService;
    public InitializeRequests(UserSettingRepository userSettingRepository){
        this.userSettingRepository=userSettingRepository;
    }

    @GetMapping("/set-session")
    public void setSessionAttributes(HttpSession sessionAttributes, UserInfo userInfo){
        SessionUtil.storeSessionValue(sessionAttributes,"username",userInfo.getUsername());
        SessionUtil.storeSessionValue(sessionAttributes,"userId",userInfo.getId());
        SessionUtil.storeSessionValue(sessionAttributes,"themeColour",getUserThemeColor(userInfo.getId()));
        SessionUtil.storeSessionValue(sessionAttributes,"userRole",userRoleService.getCurrentUserRole(userInfo));
    }

    @GetMapping("/get-session")
    public Boolean getSessionAttributes(HttpSession sessionAttributes) {
        if(sessionAttributes==null){
            return false;
        }
        String username = (String) SessionUtil.getSessionValue(sessionAttributes, "username");
        Long userId = (Long) SessionUtil.getSessionValue(sessionAttributes, "userId");
        if(username!=null && userId!=null){
            return true;
        }
        return false;

    }

    public String getUserThemeColor(Long userId){
        UserSetting userSetting;
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
        return toReturn;

    }

}

class SessionUtil {

    public static void storeSessionValue(HttpSession session, String key, Object value) {
        session.setAttribute(key, value);
    }
    public static Object getSessionValue(HttpSession session, String key) {
        return session.getAttribute(key);
    }
}