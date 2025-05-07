package com.gmi.learn;

import com.gmi.learn.dao.FoodRepository;
import com.gmi.learn.dao.impl.BookDaoImpl;
import com.gmi.learn.dao.impl.FoodDaoImpl;
import com.gmi.learn.dao.impl.UserInfoDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Bean
    public BookDaoImpl bookDaoImpl(){
        return new BookDaoImpl(jdbcTemplate);
    }

    @Bean
    public UserInfoDaoImpl userInfoDao(){
        return new UserInfoDaoImpl(jdbcTemplate);
    }

    @Bean
    public FoodDaoImpl foodDao(){
        return new FoodDaoImpl(jdbcTemplate);
    }




}
