package com.gmi.learn.controller;

import com.gmi.learn.dao.FoodRepository;
import com.gmi.learn.domain.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("api/foods")
public class FoodController {


    private FoodRepository foodRepository;

    public FoodController(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }


    @GetMapping
    public Page<Food> getFoods(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam String name,@RequestParam String sort,@RequestParam String order){


        if (!"asc".equalsIgnoreCase(order) && !"desc".equalsIgnoreCase(order)) {
            order = "asc"; // Default order if invalid
        }

        if (!Arrays.asList("name", "price","lastUpdated").contains(sort)) {
            sort = "name"; // Default sort column if invalid
        }

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable= PageRequest.of(page, size, Sort.by(direction,sort));
        return foodRepository.findByNameContainingIgnoreCase(name,pageable);
    }


}
