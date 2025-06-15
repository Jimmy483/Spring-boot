package com.gmi.learn.service;

import com.gmi.learn.DateUtils;
import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.Food;
import com.gmi.learn.repository.FoodRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodService {
    private FoodRepository foodRepository;
    public FoodService(FoodRepository foodRepository){
        this.foodRepository=foodRepository;
    }

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

    public String uploadImage(MultipartFile imageFile){
        try{
            String staticDir = new File("src/main/resources/static/uploads").getAbsolutePath();

            File dir= new File(staticDir);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String fileName = imageFile.getOriginalFilename();
            File destination = new File(dir,fileName);
            imageFile.transferTo(destination);

            return "/uploads/" + fileName;
        }catch (IOException e){
            e.printStackTrace();
            return "Upload Failed : " + e.getMessage();
        }

    }
    public void saveFood(String name, String price, String image, String lastUpdated, HttpSession httpSession){
        String createdBy= SessionUtility.getSessionValue(httpSession, "username").toString();
        Food food=new Food();
        food.setName(name);
        food.setPrice(Integer.parseInt(price));
        food.setImage(image);
        food.setLastUpdated(lastUpdated);
        food.setCreatedBy(createdBy);
        foodRepository.save(food);
    }
}
