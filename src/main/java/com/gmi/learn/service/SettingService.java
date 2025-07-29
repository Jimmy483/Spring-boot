package com.gmi.learn.service;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.UserCreateStatus;
import com.gmi.learn.domain.UserSetting;
import com.gmi.learn.repository.SettingRepository;
import com.gmi.learn.repository.UserCreateStatusRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SettingService {



private SettingRepository settingRepository;

@Autowired
private UserCreateStatusRepository userStatusRepository;

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

    public List<Map<String, Object>> getAllUserRequestsUrl(){
        List<UserCreateStatus> userCreateStatusList = userStatusRepository.findAll();
        List<Map<String, Object>> updatedStatusList= new ArrayList<>();
        for(UserCreateStatus status:userCreateStatusList){
            Map<String, Object> statusMap = new HashMap<>();
            statusMap.put("id", status.getId());
            statusMap.put("url", status.getUrl());
            statusMap.put("status", status.getStatus());
            updatedStatusList.add(statusMap);
        }
        return updatedStatusList;
    }
}
