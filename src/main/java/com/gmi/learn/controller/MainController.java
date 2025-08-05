package com.gmi.learn.controller;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.dao.impl.FoodDaoImpl;
import com.gmi.learn.dao.impl.UserInfoDaoImpl;
import com.gmi.learn.domain.Food;
import com.gmi.learn.domain.UserInfo;
import com.gmi.learn.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class MainController {


    File file=new File("src\\main\\resources\\templates\\dashboard.html");


    @Autowired
    private UserInfoDaoImpl userInfoDao;

    @Autowired
    private FoodDaoImpl foodDao;

    @Autowired
    private UserService userService;







        @RequestMapping(path = "/dashboard", method = {RequestMethod.GET, RequestMethod.POST})
        public String dashboard(Model model, HttpSession httpSession) {
            model.addAttribute("name", httpSession.getAttribute("username"));
            model.addAttribute("loggedIn",httpSession.getAttribute("username")!=null);
            System.out.println("model = " + model);
            System.out.println("theme name " + SessionUtility.getSessionValue(httpSession, "themeColour"));
            return "dashboard";
        }


        @RequestMapping(value="/search")
        @ResponseBody
        public List<Food> getFoodWithSearch(@RequestParam("name") String name){
            System.out.println("name params = " + name);
            List<Food> foodList=foodDao.fetch(name);
            System.out.println("food list = " + foodList);
            return foodList;
        }

    @RequestMapping(value="/sort")
    @ResponseBody
    public List<Food> sortTable(@RequestParam("name") String name, @RequestParam("sort") String sort, @RequestParam("order") String order){
        System.out.println("name  = " + name);
        System.out.println("sort = " + sort);
        System.out.println("order = " + order);
        List<Food> foodList=new ArrayList<>();
        return foodList;
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST)
    @ResponseBody
    public String checkUsernameAndPassword(HttpSession sessionAttributes, Model model, @RequestParam("username") String name, @RequestParam("password") String password) {

        UserInfo user=new UserInfo();

        if(userInfoDao.fetch(sessionAttributes, name,password)!=null) {
            return "true";
        }else {
            return "Incorrect Username or Password";

        }
    }


    @GetMapping(path = "/profile")
    public String goToProfile(HttpSession httpSession, Model model){
        String templateToRender="profile.html";
        Map<String, Object> retMap = userService.getUserInfoMap(httpSession);
        model.addAttribute("data",retMap);
        model.addAttribute("templateToRender",templateToRender);
        return "profileGeneric";
    }



    @GetMapping(path="/error")
    public String errorPage(){
        return "error";
    }


}


// create a platform where people can check the average prices for stuffs