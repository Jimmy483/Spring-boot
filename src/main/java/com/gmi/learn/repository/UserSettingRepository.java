package com.gmi.learn.repository;

import com.gmi.learn.domain.UserSetting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  UserSettingRepository extends CrudRepository<UserSetting, Long> {

    UserSetting findByUserId(Long userId);
}
