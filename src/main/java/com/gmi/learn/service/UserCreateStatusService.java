package com.gmi.learn.service;

import com.gmi.learn.domain.UserCreateStatus;
import com.gmi.learn.repository.UserCreateStatusRepository;
import com.gmi.learn.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class UserCreateStatusService {


    private UserCreateStatusRepository userCreateStatusRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public UserCreateStatusService(UserCreateStatusRepository userCreateStatusRepository){
        this.userCreateStatusRepository = userCreateStatusRepository;
    }

    public void createStatusUrl(){
        createUserStatusRequest(System.currentTimeMillis()+"");
    }

    public Boolean checkIfExistingRequest(){
        Optional<UserCreateStatus> userCreateStatus = userCreateStatusRepository.findByStatus("Pending");
        if(userCreateStatus.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public Boolean checkIfRequestExist(String url){
        Optional<UserCreateStatus> status = userCreateStatusRepository.findByUrlAndStatus(url, "pending");
        return status.isPresent();
    }

    public void createUserStatusRequest(String url){
        UserCreateStatus status = new UserCreateStatus();
        status.setUrl(url);
        status.setStatus("pending");
        userCreateStatusRepository.save(status);
    }

    public void updateUserStatusRequest(String url){
        System.out.println("url = " + url);
        Optional<UserCreateStatus> statusOptional = userCreateStatusRepository.findByUrlAndStatus(url,"pending");
        if(statusOptional.isPresent()){
            System.out.println("status = " + statusOptional.get());
            UserCreateStatus status = statusOptional.get();
            status.setStatus("completed");
            userCreateStatusRepository.save(status);
        }
    }
}
