package com.gmi.learn.dao;

import com.gmi.learn.domain.UserSetting;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserSettingRepository extends CrudRepository<UserSetting, Long> {

    UserSetting findByUserId(Long userId);
}
