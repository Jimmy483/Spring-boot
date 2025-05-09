package com.gmi.learn.dao;

import com.gmi.learn.domain.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FoodDao {


     List<Food> fetch(String name);

     List<Food> fetchWithSortOrder(String name, String sort, String order);
}
