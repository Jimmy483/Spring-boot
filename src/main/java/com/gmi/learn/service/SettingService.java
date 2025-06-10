package com.gmi.learn.service;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.UserSetting;
import com.gmi.learn.repository.SettingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class SettingService {



SettingRepository settingRepository;

public SettingService(SettingRepository settingRepository){
    this.settingRepository=settingRepository;
}

    @SuppressWarnings("unchecked")
    public void UpdateSetting(HttpSession session, String theme){
        try{
            Optional<UserSetting> userSettingOpt=settingRepository.findById(Long.parseLong(SessionUtility.getSessionValue(session,"userId").toString()));
            if(userSettingOpt.isPresent()){
                UserSetting userSetting=userSettingOpt.get();
                userSetting.setTheme(theme);
                settingRepository.save(userSetting);
            }else {
                System.out.println("User's setting not found");
            }

        }catch (Throwable e){
            System.out.println("Something went wrong = " + e);
        }
        SessionUtility.storeSessionValue(session,"themeColour",theme);

//        query here

    }
}
