package com.gmi.learn.controller;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.dao.impl.FoodDaoImpl;
import com.gmi.learn.domain.Food;
import com.gmi.learn.service.UserService;
import jakarta.servlet.http.HttpSession;
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
    private FoodDaoImpl foodDao;

    @Autowired
    private UserService userService;


        @RequestMapping(path = "/dashboard", method = {RequestMethod.GET, RequestMethod.POST})
        public String dashboard(Model model, HttpSession httpSession) {
            model.addAttribute("name", httpSession.getAttribute("username"));
            model.addAttribute("loggedIn",httpSession.getAttribute("username")!=null);
            return "dashboard";
        }


        @RequestMapping(value="/search")
        @ResponseBody
        public List<Food> getFoodWithSearch(@RequestParam("name") String name){
            List<Food> foodList=foodDao.fetch(name);
            return foodList;
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
