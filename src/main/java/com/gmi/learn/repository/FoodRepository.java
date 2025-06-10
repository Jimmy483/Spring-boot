package com.gmi.learn.repository;

import com.gmi.learn.domain.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FoodRepository extends PagingAndSortingRepository<Food, Long> {

    Page<Food> findByNameContainingIgnoreCase(String name,Pageable pageable);
}
