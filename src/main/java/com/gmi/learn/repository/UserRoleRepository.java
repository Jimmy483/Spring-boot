package com.gmi.learn.repository;

import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    Optional<UserRole> findByUserInfo(UserInfo userInfo);
}
