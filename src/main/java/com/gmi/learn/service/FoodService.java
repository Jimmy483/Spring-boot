package com.gmi.learn.service;

import com.gmi.learn.DateUtils;
import com.gmi.learn.domain.Food;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class FoodService {

    public Map<String,Object> convertDateToDaysAndUpdateFood(Page<Food> foodList){

        Map<String,Object> response = new HashMap<>();
        List<Map<String,Object>> foodArrayList=foodList.getContent().stream()
                .map(food -> {
                    Map<String,Object> map =new HashMap<>();
                    map.put("id",food.getId());
                    map.put("name",food.getName());
                    map.put("price",food.getPrice());
                    map.put("image",food.getImage());
                    map.put("lastUpdated",formatPeriod(DateUtils.getDateDifference(LocalDate.parse(food.getLastUpdated()), LocalDate.now())));
                    return map;
                }).collect(Collectors.toList());

        response.put("content",foodArrayList);
        response.put("pageNumber",foodList.getNumber());
        response.put("totalItems", foodList.getTotalElements());
        response.put("totalPages",foodList.getTotalPages());
        response.put("pageSize",foodList.getSize());
        response.put("sort",foodList.getSort());
        return  response;

    }

    private String formatPeriod(Period period) {
        if (period.getYears() > 0) {
            if(period.getYears()==1)
                return "A year ago";
            return period.getYears() + " years ago";
        } else if (period.getMonths() > 0) {
            if(period.getMonths()==1)
                return "A month ago";

            return period.getMonths() + " months ago";
        } else {
            if(period.getDays()==1)
                return "A day ago";
            return period.getDays() + " days ago";
        }
    }
}
