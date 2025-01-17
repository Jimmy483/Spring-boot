package com.gmi.learn.dao;

import com.gmi.learn.domain.UserInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.SessionAttributes;

public interface UserInfoDao {

    UserInfo fetch(HttpSession sessionAttributes, String username, String password);
}
