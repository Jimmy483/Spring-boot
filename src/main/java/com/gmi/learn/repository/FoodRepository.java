package com.gmi.learn.repository;

import com.gmi.learn.domain.Food;
import com.gmi.learn.domain.UserSetting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    Page<Food> findByNameContainingIgnoreCase(String name,Pageable pageable);
    Page<Food> findByNameContainingIgnoreCaseAndIsDeleted(String name, Boolean isDeleted, Pageable pageable);
    List<Food> findAllByName(String name);

}
