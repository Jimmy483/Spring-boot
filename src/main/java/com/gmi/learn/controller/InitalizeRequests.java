package com.gmi.learn.controller;
import com.gmi.learn.domain.UserInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("username")
public class InitalizeRequests {

    @GetMapping("/set-session")
    public void setSessionAttributes(HttpSession sessionAttributes, List<UserInfo> userInfo){
//        model.addAttribute("username","Jimmy");
        SessionUtils.storeSessionValue(sessionAttributes,"username",userInfo.get(0).getUsername());
        SessionUtils.storeSessionValue(sessionAttributes,"userId",userInfo.get(0).getId());
    }

    @GetMapping("/get-session")
    public Boolean getSessionAttributes(HttpSession sessionAttributes) {
        // Fetch session values using the SessionUtils class
        String username = (String) SessionUtils.getSessionValue(sessionAttributes, "username");
        Long userId = (Long) SessionUtils.getSessionValue(sessionAttributes, "userId");

        // Add session values to the model to display in the view (optional)
//        model.addAttribute("username", username);
//        model.addAttribute("userId", userId);

        System.out.println("username = " + username);
        System.out.println("userId = " + userId);
        if(!username.isEmpty() && userId!=null){
            return true;
        }else{
            return false;
        }
        // Return a view name (change to your actual view)
//        return "showSessionAttributes"; // This is the view where you'll display the session attributes
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