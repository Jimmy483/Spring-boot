package com.gmi.learn.controller;

import com.gmi.learn.domain.Food;
import com.gmi.learn.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FoodController {


    @Autowired
    private FoodService foodService;

    public FoodController() {
    }


    @GetMapping(path="api/foods")
    @ResponseBody
    public Map<String, Object> getFoods(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String name, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, @RequestParam(defaultValue = "false") String fromPagination){

        Map<String, Object> returnMap=foodService.getFood(sort, order, page, name, size, fromPagination, false);
        return returnMap;

    }

    @GetMapping(path="/search")
    @ResponseBody
    public List<Food> searchFood(@RequestParam("name") String name){
        return foodService.getFoodListWithName(name);
    }




}
