package com.gmi.learn.controller;

import com.gmi.learn.domain.Food;
import com.gmi.learn.service.FoodService;
import com.gmi.learn.service.SharedServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
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

    @PostMapping(path="/addItem")
    @ResponseBody
    public String saveItem(@RequestParam("name") String itemName, @RequestParam("price") String price, @RequestParam("image") MultipartFile image, HttpSession httpSession){
        if(image.isEmpty()){
            return "No image selected";
        }
        System.out.println("Success la");
//        String fileName='/' + image.getOriginalFilename();

        String fileName = foodService.uploadImage(image);
        System.out.println("image name = " + fileName);
        foodService.saveFood(itemName, price, fileName, LocalDate.now().toString(), httpSession);

        return "success";
    }


}
