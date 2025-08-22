package com.gmi.learn.repository;

import com.gmi.learn.domain.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<UserSetting, Long> {

}
