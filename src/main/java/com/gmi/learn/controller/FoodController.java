package com.gmi.learn.controller;

import com.gmi.learn.dao.FoodRepository;
import com.gmi.learn.domain.Food;
import com.gmi.learn.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Period;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/foods")
public class FoodController {


    private FoodRepository foodRepository;

    @Autowired
    private FoodService foodService;

    public FoodController(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }


    @GetMapping
    @ResponseBody
    public Map<String, Object> getFoods(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String name, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, @RequestParam(defaultValue = "false") String fromPagination){


//        move code to service
        Pageable pageable;
        Page<Food> food;
        Sort.Direction direction;
        Map<String, Object> returnMap= new HashMap<>();
        Map<String, Object> jpt= new HashMap<>();
        System.out.println("page = " + page);

        System.out.println("sort" + sort);
        if(sort!=null){
            if (!Arrays.asList("name", "price","lastUpdated").contains(sort)) {
                sort = "name"; // Default sort column if invalid
            }
            System.out.println("sort here = " + sort);
            if(fromPagination.equals("false")){
                direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
            }else {
                direction ="desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
            }
            System.out.println("order here = " + direction);
            pageable= PageRequest.of(page, size, Sort.by(direction,sort));
            System.out.println("page first = " + pageable);

        }else {
            direction = Sort.Direction.ASC;
            pageable= PageRequest.of(page,size);
            System.out.println("page scond = " + pageable);

        }
        food= foodRepository.findByNameContainingIgnoreCase(name,pageable);
        returnMap.put("order",direction);
        returnMap.put("row",foodService.convertDateToDaysAndUpdateFood(food));
        return returnMap;

    }


}
