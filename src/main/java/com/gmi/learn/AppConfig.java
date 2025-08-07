package com.gmi.learn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;


//    @Bean
//    public BookDaoImpl bookDaoImpl(){
//        return new BookDaoImpl(jdbcTemplate);
//    }

//    @Bean
//    public UserInfoDaoImpl userInfoDao(){
//        return new UserInfoDaoImpl(jdbcTemplate);
//    }

//    @Bean
//    public FoodDaoImpl foodDao(){
//        return new FoodDaoImpl(jdbcTemplate);
//    }

//    @Bean
//    public FoodService foodService(){return new FoodService();}


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
