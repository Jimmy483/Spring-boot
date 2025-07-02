package com.gmi.learn.dao.impl;

import com.gmi.learn.InitalizeRequests;
import com.gmi.learn.dao.UserInfoDao;
import com.gmi.learn.domain.UserInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class UserInfoDaoImpl implements UserInfoDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    InitalizeRequests requests;

    public UserInfoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserInfo fetch(HttpSession sessionAttributes, String username, String password) {

        System.out.println("username = " + username);
        System.out.println("password = " + password);
        List<UserInfo> users=new ArrayList<>();
        try{
            users = jdbcTemplate.query(
                    "SELECT * FROM userInfo WHERE username = ? and passwd = ? LIMIT 1",
                    new Object[]{username, password},
                    (rs, rowNum) -> new UserInfo(
                            rs.getLong("id"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("username"),
                            rs.getBoolean("isAdmin")
                    )
            );
            System.out.println("result = " + users);

        }catch (EmptyResultDataAccessException e){
            System.out.println();

        }


        if(users.isEmpty())
            return null;
        else{
            requests.setSessionAttributes(sessionAttributes, users);
            return users.get(0);
        }


    }

//    public String callFetch(String username, String password){
//        String user=fetch(username, password);
//        return user;
//    }
}
