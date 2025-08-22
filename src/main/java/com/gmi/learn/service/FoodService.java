package com.gmi.learn.service;

import com.gmi.learn.DateUtils;
import com.gmi.learn.SessionUtility;
import com.gmi.learn.domain.Food;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.FoodRepository;
import com.gmi.learn.repository.UserInfoRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private UserInfoRepository userInfoRepository;


    public FoodService(FoodRepository foodRepository, UserInfoRepository userInfoRepository){
        this.foodRepository=foodRepository;
        this.userInfoRepository = userInfoRepository;
    }


    public Map<String, Object> getFood(String sort, String order, int page, String name, int size, String fromPagination, Boolean showDeleted){
        Pageable pageable;
        Page<Food> food;
        Sort.Direction direction;
        Map<String, Object> returnMap= new HashMap<>();

        if(sort!=null){
            if (!Arrays.asList("name", "price","lastUpdated").contains(sort)) {
                sort = "name"; // Default sort column if invalid
            }
            if(fromPagination.equals("false")){
                direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
            }else {
                direction ="desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
            }
            pageable= PageRequest.of(page, size, Sort.by(direction,sort));

        }else {
            direction = Sort.Direction.ASC;
            pageable= PageRequest.of(page,size);

        }
        if(showDeleted){
            food= foodRepository.findByNameContainingIgnoreCase(name,pageable);
        }else
            food = foodRepository.findByNameContainingIgnoreCaseAndIsDeleted(name, false ,pageable);

        returnMap.put("order",direction);
        returnMap.put("row",convertDateToDaysAndUpdateFood(food));
        return returnMap;
    }

    public Map<String,Object> convertDateToDaysAndUpdateFood(Page<Food> foodList){

        Map<String,Object> response = new HashMap<>();
        List<Map<String,Object>> foodArrayList=foodList.getContent().stream()
                .map(food -> {
                    String updatedByUserName = food.getUpdatedBy()!=null?getFoodRelatedUserName(food.getUpdatedBy()):null;
                    String createdByUserName = food.getCreatedBy()!=null?getFoodRelatedUserName(food.getCreatedBy()):null;
                    Map<String,Object> map =new HashMap<>();
                    map.put("id",food.getId());
                    map.put("name",food.getName());
                    map.put("price",food.getPrice());
                    map.put("image",food.getImage());
                    map.put("updatedBy", updatedByUserName);
                    map.put("createdBy", createdByUserName);
                    map.put("isDeleted", food.getIsDeleted());
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

    public String getFoodRelatedUserName(Long id){
        Optional<UserInfo> optionalUpdatedByUser = userInfoRepository.findById(id);
        if(optionalUpdatedByUser.isPresent()){
            return optionalUpdatedByUser.get().getUsername();
        }else{
            return null;
        }
    }

    protected String formatPeriod(Period period) {
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
            String staticDir = System.getProperty("user.home") + "/uploads/items/";
            File dir= new File(staticDir);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String fileName = imageFile.getOriginalFilename();
            File destination = new File(dir,fileName);
            imageFile.transferTo(destination);

            return "/uploads/items/" + fileName;
        }catch (IOException e){
            e.printStackTrace();
            return "Upload Failed : " + e.getMessage();
        }

    }
    public void saveFood(String name, int price, String image, String lastUpdated, HttpSession httpSession){
        Long createdBy= Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        Food food=new Food();
        food.setName(name);
        food.setPrice(price);
        if(!image.equals("")){
            food.setImage(image);
        }
        food.setLastUpdated(lastUpdated);
        food.setCreatedBy(createdBy);
        food.setIsDeleted(false);
        foodRepository.save(food);
    }

    public void updateFood(String name, int price, String image, String lastUpdated, HttpSession httpSession, long id){
        Long updatedBy = Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        Optional<Food> optionalFood = foodRepository.findById(id);
        if(optionalFood.isPresent()){
            Food food = optionalFood.get();
            food.setName(name);
            food.setPrice(price);
            if(!image.equals("")){
                food.setImage(image);
            }
            food.setLastUpdated(lastUpdated);
            food.setUpdatedBy(updatedBy);
            foodRepository.save(food);
        }else {
            System.out.println("row for id not found");
        }
    }

    public void deleteOrRestoreFood(long itemId, String action, HttpSession httpSession){
        Long updatedBy= Long.parseLong(SessionUtility.getSessionValue(httpSession, "userId").toString());
        Optional<Food> optionalFood = foodRepository.findById(itemId);
        if(optionalFood.isPresent()){
            Food food = optionalFood.get();
            if(action.equals("delete")){
                food.setIsDeleted(true);
            }else{
                food.setIsDeleted(false);
            }
            food.setLastUpdated(LocalDate.now().toString());
            food.setUpdatedBy(updatedBy);
            foodRepository.save(food);
        }else{
            System.out.println("Could not find the item");
        }


    }

    public Map<String, Object> getReturnMap(long id){
        Map<String, Object> foodMap = new HashMap<>();
        Optional<Food> optionalFood=foodRepository.findById(id);
        if(optionalFood.isPresent()){
            Food food=optionalFood.get();
            foodMap.put("id", food.getId());
            foodMap.put("name", food.getName());
            foodMap.put("price", food.getPrice());
            foodMap.put("image", food.getImage());
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("food",foodMap);
        return returnMap;
    }


    public List<Food> getFoodListWithName(String name){
        return foodRepository.findAllByName(name);
    }
}
