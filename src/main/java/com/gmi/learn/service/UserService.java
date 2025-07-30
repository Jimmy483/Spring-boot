package com.gmi.learn.service;

import com.gmi.learn.domain.UserCreateStatus;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserCreateStatusService userCreateStatusService;

    public UserService(UserInfoRepository userInfoRepository){
        this.userInfoRepository=userInfoRepository;
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

//    public void createUser(String firstName, String lastName, String username, String password){
//        UserInfo userInfo = new UserInfo();
//        userInfo.setFirstName(firstName);
//        userInfo.setLastName(lastName);
//        userInfo.setUsername(username);
//        userInfo.setPasswd(password);
//        userInfoRepository.save(userInfo);
//
//    }

    public void createUser(UserInfo userInfo){
        userInfoRepository.save(userInfo);
    }
}
