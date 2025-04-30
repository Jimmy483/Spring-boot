package com.gmi.learn.dao;

import com.gmi.learn.domain.Food;

import java.util.List;

public interface FoodDao {

     List<Food> fetch(String name);

     List<Food> fetchWithSortOrder(String name, String sort, String order);
}
