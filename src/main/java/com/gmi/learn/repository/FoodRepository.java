package com.gmi.learn.repository;

import com.gmi.learn.domain.Food;
import com.gmi.learn.domain.UserSetting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

    Page<Food> findByNameContainingIgnoreCase(String name,Pageable pageable);


}
