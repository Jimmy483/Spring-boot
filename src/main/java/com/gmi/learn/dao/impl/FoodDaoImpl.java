package com.gmi.learn.dao.impl;

import com.gmi.learn.dao.FoodDao;
import com.gmi.learn.domain.Food;
import com.gmi.learn.domain.UserInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class FoodDaoImpl implements FoodDao {

    private JdbcTemplate jdbcTemplate;

    public FoodDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }



    @Override
    public List<Food> fetchWithSortOrder(String name, String sort, String order) {
        List<Food> foodList = new ArrayList<>();
        System.out.println("name of = " + name);
        System.out.println("sort " + sort);
        System.out.println("order " + order);

        if (!"asc".equalsIgnoreCase(order) && !"desc".equalsIgnoreCase(order)) {
            order = "asc"; // Default order if invalid
        }

        if (!Arrays.asList("name", "price","lastUpdated").contains(sort)) {
            sort = "name"; // Default sort column if invalid
        }
        String query="Select id, name, price, image, DATE_FORMAT(lastUpdated,'%d-%m-%Y') as lastUpdated from food where name like ? order by " + sort + " " + order;
        System.out.println("query sort = " + query);

        try{
            foodList=jdbcTemplate.query(query,new Object[]{"%" + name + "%"},
                    (rs, rowNum) ->new Food(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getLong("price"),
                            rs.getString("image"),
                            rs.getLong("lastUpdated"),
                            rs.getLong("createdBy"),
                            rs.getString("updatedBy"),
                            rs.getBoolean("isDeleted")
                    )
            );

        }catch (Exception e){
            e.printStackTrace();
        }

        return foodList;
    }

    @Override
    public List<Food> fetch(String name) {
        List<Food> foodList = new ArrayList<>();
        System.out.println("name of = " + name);
        foodList=jdbcTemplate.query("Select id, name, price, image, DATE_FORMAT(lastUpdated,'%d-%m-%Y') as lastUpdated from food where name like ?",new Object[]{"%" + name + "%"},
                (rs, rowNum) ->new Food(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("price"),
                        rs.getString("image"),
                        rs.getLong("lastUpdated"),
                        rs.getLong("createdBy"),
                        rs.getString("updatedBy"),
                        rs.getBoolean("isDeleted")
                )
        );

        return foodList;
    }
}
