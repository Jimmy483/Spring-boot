package com.gmi.learn.service;

import com.gmi.learn.InitializeRequests;
import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.UserCreateStatus;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.UserInfoRepository;
import com.gmi.learn.repository.UserRoleRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserService {

    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserCreateStatusService userCreateStatusService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final String defaultDisplayImage = "/profilePics/defaultDP.jpg";

    @Autowired
    InitializeRequests requests;

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


    public Boolean updateUserInfo(HttpSession httpSession, String firstName, String lastName,MultipartFile file){
        String image=null;
        Long userId = Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        if(file!=null){
            image = uploadImage(file);
            if(image==null){
                return false;
            }
        }
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);
        if(userInfoOptional.isPresent()){
            UserInfo userInfo = userInfoOptional.get();
            userInfo.setFirstName(firstName);
            userInfo.setLastName(lastName);
            if(image!=null){
                userInfo.setDisplayPicture(image);
            }
            userInfoRepository.save(userInfo);
            return true;
        }else{
            return false;
        }
    }

    public String uploadImage(MultipartFile file) {
        try{
            String staticDir = System.getProperty("user.home") + "/uploads/profilePics/";
            File dir = new File(staticDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String fileName = file.getOriginalFilename();
            File destination = new File(dir, fileName);
            file.transferTo(destination);
            return "/uploads/profilePics/"+fileName;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }

    public Map<String, Object> getAllUserMap(){
        List<UserInfo> userInfoList = userInfoRepository.findAllExceptAdminRole();
        Map<String, Object> retMap = new HashMap<>();
        List<Map<String, Object>> newUserList = new ArrayList<>();
        for(UserInfo userInfo: userInfoList){
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", userInfo.getId());
            userMap.put("userName",userInfo.getUsername());
            userMap.put("userRole",userRoleService.getCurrentUserRole(userInfo));
            newUserList.add(userMap);
        }
        retMap.put("userList", newUserList);
        retMap.put("roleList", getAllRoleExceptAdmin());

        return retMap;
    }

    public List<String> getAllRoleExceptAdmin(){
        return new ArrayList<>(Arrays.asList("None","Moderator", "Assistant"));
    }



    public List<Map<String, Object>> getAllUserBySearchName(String search){

        List<UserInfo> userInfoList = userInfoRepository.findAllByUsernameContaining(search);
        List<Map<String, Object>> newUserList=new ArrayList<>();
        for(UserInfo userInfo : userInfoList){
            Map<String, Object> userInfoMap = new HashMap<>();
            userInfoMap.put("id", userInfo.getId());
            userInfoMap.put("username", userInfo.getUsername());
            userInfoMap.put("image", userInfo.getDisplayPicture()!=null?userInfo.getDisplayPicture():defaultDisplayImage);
            newUserList.add(userInfoMap);
        }
        return newUserList;
    }

    public Map<String, Object> getUserInfoMap(HttpSession httpSession){
        Long userId = Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);
        Map<String, Object> userMap = new HashMap<>();
        if(userInfoOptional.isPresent()){
            UserInfo userInfo=userInfoOptional.get();
            userMap.put("firstName",userInfo.getFirstName());
            userMap.put("lastName",userInfo.getLastName());
            userMap.put("username", userInfo.getUsername());
            userMap.put("displayPic", userInfo.getDisplayPicture()!=null?userInfo.getDisplayPicture():defaultDisplayImage);
        }
        return userMap;
    }

    public void updateUserRole(String role, Long userId){
        System.out.println("userId = " + userId);
        System.out.println("role = " + role);
        userRoleService.changeUserRole(getUserInfo(userId),role);
    }

    public void createUser(UserInfo userInfo){
        System.out.println("user pass = " + userInfo.getPasswd());
        System.out.println("user hashed pass = " + passwordEncoder.encode(userInfo.getPasswd()));
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setFirstName(userInfo.getFirstName());
        newUserInfo.setLastName(userInfo.getLastName());
        newUserInfo.setUsername(userInfo.getUsername());
        newUserInfo.setPasswd(passwordEncoder.encode(userInfo.getPasswd()));
        userInfoRepository.save(newUserInfo);
    }

    public Boolean checkPassword(HttpSession httpSession, String password){
        System.out.println("password = " + password);
        long userId = Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        System.out.println("user id = " + userId);
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);
        System.out.println("userInfo = " + userInfoOptional);
        if(userInfoOptional.isPresent()){
            UserInfo userInfo = userInfoOptional.get();
            if(!passwordEncoder.matches(password,userInfo.getPasswd())){
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

    public void changePassword(UserInfo userInfo){
        System.out.println("userInfo change =" +userInfo);
        String hashedPass = passwordEncoder.encode(userInfo.getPasswd());
        Optional<UserInfo> userInfoOptional= userInfoRepository.findById(userInfo.getId());
        if(userInfoOptional.isPresent()){
            UserInfo userInfo1 = userInfoOptional.get();
            userInfo1.setPasswd(hashedPass);
            userInfoRepository.save(userInfo1);
        }
    }

    public Boolean checkLoginInfo(String username, String password, HttpSession httpSession){
        System.out.println("password first " + password);
        System.out.println("password hased = " + passwordEncoder.encode(password));
        Optional<UserInfo> userInfoOptional = userInfoRepository.findByUsername(username);
        if(userInfoOptional.isPresent()){
            UserInfo userInfo = userInfoOptional.get();
            if(!passwordEncoder.matches(password,userInfo.getPasswd())){
                return false;
            }
            requests.setSessionAttributes(httpSession, userInfo);
            return true;
        }
        return false;

    }

    public Boolean checkIfUserExists(String username){
        Optional<UserInfo> userInfoOptional = userInfoRepository.findByUsername(username);
        if(userInfoOptional.isPresent()){
            return  true;
        }
        return false;
    }
}
