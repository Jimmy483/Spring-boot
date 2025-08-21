package com.gmi.learn.service;
import com.gmi.learn.DateUtils;
import com.gmi.learn.domain.Food;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.repository.FoodRepository;
import com.gmi.learn.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class FoodServiceTest{

    @Mock
    FoodRepository foodRepository;

    @Mock
    UserInfoRepository userInfoRepository;

    @InjectMocks
    FoodService foodService;

    @Test
    void testGetFood(){
        Pageable pageable = PageRequest.of(0, 5);
        Food food = new Food();
        food.setName("Test1");
        food.setPrice(200);
        food.setCreatedBy(1L);
        food.setIsDeleted(false);
        food.setLastUpdated(LocalDate.now().toString());
        List<Food> foodList = List.of(food);
        Page<Food> pageImpl = new PageImpl<>(foodList,pageable, foodList.size());

        when(foodRepository.findByNameContainingIgnoreCaseAndIsDeleted("Test1",false,pageable)).
                thenReturn(pageImpl);

        Map<String, Object> foodMap = foodService.getFood(null, null,0, "Test1",5,"false", false);


        assertTrue(foodMap.containsKey("order"));
        assertTrue(foodMap.containsKey("row"));
    }

    @Test
    void sevenTeenShouldReturnOscar(){
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("Test");
        userInfo.setPasswd("Test");
        userInfo.setFirstName("Test");
        userInfo.setLastName("Test");


        UserInfo savedUser = new UserInfo();
        savedUser.setId(17L);
        savedUser.setUsername("oscar");
        savedUser.setPasswd("oscar");

        when(userInfoRepository.save(any(UserInfo.class))).thenReturn(savedUser);

        String username= userInfoRepository.save(userInfo).getUsername();

        assertEquals("oscar", username);
        assertNotNull(username);

    }

    @Test
    void idShouldReturnUsername(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUsername("Test");
        userInfo.setPasswd("Test");

        Optional<UserInfo> userInfoOptional = Optional.of(userInfo);

        when(userInfoRepository.findById(1L)).thenReturn(userInfoOptional);

        String username = foodService.getFoodRelatedUserName(1L);

        assertEquals("Test", username);
    }

    @Test
    void MayFiveOfCurrentYearShouldReturnFiveMonthsAgo(){
        LocalDate initialDate = LocalDate.of(2025, 5,5);

        assertEquals("3 months ago", foodService.formatPeriod(DateUtils.getDateDifference(initialDate, LocalDate.now())));


    }

    @Test
    void JulyOfLastYearShouldReturnAYearAgo(){
        LocalDate initialDate = LocalDate.of(2024, 7,1);

        assertEquals("A year ago", foodService.formatPeriod(DateUtils.getDateDifference(initialDate, LocalDate.now())));
    }

}