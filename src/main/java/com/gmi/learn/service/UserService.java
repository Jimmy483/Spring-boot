package com.gmi.learn.service;

import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserInfoRepository userInfoRepository;

    public UserService(UserInfoRepository userInfoRepository){
        this.userInfoRepository=userInfoRepository;
    }

    public UserInfo getAdminInfo(){
        return userInfoRepository.findByIsAdminTrue();
    }
}
