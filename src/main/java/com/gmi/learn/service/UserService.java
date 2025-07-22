package com.gmi.learn.service;

import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private UserInfoRepository userInfoRepository;

    public UserService(UserInfoRepository userInfoRepository){
        this.userInfoRepository=userInfoRepository;
    }

    public UserInfo getAdminInfo(){
        return userInfoRepository.findByIsAdminTrue();
    }

    public UserInfo getUserInfo(long id){
        Optional<UserInfo> userInfoOptional= userInfoRepository.findById(id);
        if(userInfoOptional.isPresent()){
            UserInfo userInfo = userInfoOptional.get();
            return userInfo;
        }else{
            return null;
        }
    }

    public UserInfo getUserInfoByUserName(String username){
        Optional<UserInfo> userInfoOptional = userInfoRepository.findByUsername(username);
        if(userInfoOptional.isPresent()){
            UserInfo userInfo=userInfoOptional.get();
            return userInfo;
        }else {
            return null;
        }
    }


    public List<Map<String, Object>> getAllUserBySearchName(String search){

        List<UserInfo> userInfoList = userInfoRepository.findAllByUsernameContaining(search);
        List<Map<String, Object>> newUserList=new ArrayList<>();
        for(UserInfo userInfo : userInfoList){
            Map<String, Object> userInfoMap = new HashMap<>();
            userInfoMap.put("id", userInfo.getId());
            userInfoMap.put("username", userInfo.getUsername());
            userInfoMap.put("image", userInfo.getDisplayPicture());
            newUserList.add(userInfoMap);
        }
        System.out.println("all list of maps = " + newUserList );
        return newUserList;
    }
}
