package com.gmi.learn.dao.impl;

import com.gmi.learn.dao.FoodDao;
import com.gmi.learn.domain.Food;
import com.gmi.learn.domain.UserInfo;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class FoodDaoImpl implements FoodDao {

    private JdbcTemplate jdbcTemplate;

    public FoodDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    @Override
    public List<Food> fetch(String name) {
        List<Food> foodList = new ArrayList<>();

        foodList=jdbcTemplate.query("Select * from food where name like ?",new Object[]{"%" + name + "%"},
                (rs, rowNum) ->new Food(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("price")
                )
                );

        return foodList;
    }
}
