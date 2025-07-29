package com.gmi.learn.service;

import com.gmi.learn.domain.UserCreateStatus;
import com.gmi.learn.repository.UserCreateStatusRepository;
import com.gmi.learn.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCreateStatusService {

    private UserCreateStatusRepository userCreateStatusRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public UserCreateStatusService(UserCreateStatusRepository userCreateStatusRepository){
        this.userCreateStatusRepository = userCreateStatusRepository;
    }

    public Boolean createStatusUrl(){
        long maxId = userInfoRepository.findMaxId();
        String url = "/create/"+(maxId+1);
        if(!checkIfExistingRequest()){
            createUserStatusRequest(url);
            return true;
        }else{
            return false;
        }


    }

    public Boolean checkIfExistingRequest(){
        Optional<UserCreateStatus> userCreateStatus = userCreateStatusRepository.findByStatus("Pending");
        if(userCreateStatus.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public void createUserStatusRequest(String url){
        UserCreateStatus status = new UserCreateStatus();
        status.setUrl(url);
        status.setStatus("pending");
        userCreateStatusRepository.save(status);
    }
}
