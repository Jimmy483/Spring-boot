package com.gmi.learn.repository;

import com.gmi.learn.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByUsername(String username);

    List<UserInfo> findAllByUsernameContaining(String search);

    @Query("Select Max(id) from UserInfo")
    long findMaxId();
}
