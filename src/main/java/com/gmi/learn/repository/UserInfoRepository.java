package com.gmi.learn.repository;

import com.gmi.learn.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByIsAdminTrue();
}
