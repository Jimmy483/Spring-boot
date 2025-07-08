package com.gmi.learn.service;

import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.domain.UserRole;
import com.gmi.learn.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleService {

    private UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository){
        this.userRoleRepository = userRoleRepository;
    }

    public String getCurrentUserRole(UserInfo userInfo){
       Optional<UserRole> userInfoOptional = userRoleRepository.findByUserInfo(userInfo);
       if(userInfoOptional.isPresent()){
           return userInfoOptional.get().getRole();
       }else {
           return "";
       }
    }

}
