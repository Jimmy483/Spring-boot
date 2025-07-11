package com.gmi.learn.repository;

import com.gmi.learn.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByIsAdminTrue();

    Optional<UserInfo> findByUsername(String username);
}
