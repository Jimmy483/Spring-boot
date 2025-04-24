package com.gmi.learn.controller;

import com.gmi.learn.dao.FoodDao;
import com.gmi.learn.dao.impl.BookDaoImpl;
import com.gmi.learn.dao.impl.FoodDaoImpl;
import com.gmi.learn.dao.impl.UserInfoDaoImpl;
import com.gmi.learn.domain.Book;
import com.gmi.learn.domain.Food;
import com.gmi.learn.domain.UserInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


@Controller
public class HelloWorldController {


    File file=new File("src\\main\\resources\\templates\\dashboard.html");

//    @Autowired
//    private BookDaoImpl underTest;

    @Autowired
    private UserInfoDaoImpl userInfoDao;

    @Autowired
    private FoodDaoImpl foodDao;

//    public HelloWorldController(UserInfoDaoImpl userInfoDao){
//        this.userInfoDao=userInfoDao;
//    }



//    @Autowired
//    private final JdbcTemplate jdbcTemplate;




    //    byte[] content = Files.readAllBytes(name);
    @GetMapping(path = "/hello")
    public String helloWorld(){

//        StringBuilder html=new StringBuilder();
//
//
//        try{
//            BufferedReader bf= new BufferedReader(new FileReader(file.getAbsolutePath()));
//            String line;
//
//            while((line=bf.readLine())!=null){
//                html.append(line);
//                html.append(System.lineSeparator());
//            }
//        }catch (IOException e){
//
//            System.out.println(e);
//        }


        return "loginPage";
    }

//        @PostMapping("/dashboard")  // Ensure you're using POST and not GET
        @RequestMapping(value = "/dashboard", method = {RequestMethod.GET, RequestMethod.POST})  // Accept both GET and POST
        public String dashboard(Model model, HttpSession httpSession) {
            model.addAttribute("name", httpSession.getAttribute("username"));  // You can pass more model attributes if necessary
            System.out.println("model = " + model);
            return "dashboard";
        }


        @RequestMapping(value="/search")
        @ResponseBody
        public List<Food> getFoodWithSearch(@RequestParam("name") String name){
            List<Food> foodList=foodDao.fetch(name);
            System.out.println("food list = " + foodList);
            return foodList;
        }


    //    @PostMapping("/home")  // Ensure you're using POST and not GET
    @RequestMapping(value = "/home", method = RequestMethod.POST)
    @ResponseBody
    public String checkUsernameAndPassword(HttpSession sessionAttributes, Model model, @RequestParam("username") String name, @RequestParam("password") String password) {
        // Process the username and password here
        System.out.println("Username: " + name);
        System.out.println("Password: " + password);

//        model.addAttribute("name", name);  // You can pass more model attributes if necessary

        UserInfo user=new UserInfo();


        if(userInfoDao.fetch(sessionAttributes, name,password)!=null) {
            return "true";
        }else {
            return "Incorrect Username or Password";  // Return the view name, like a Thymeleaf template

        }
    }

//    responsBody to return as a string instead of a template
    @ResponseBody
    @GetMapping(path="/underConst")
    public String pageYetToExist(){
        return "Feature Does Not Exist Yet";
    }

    @GetMapping(path="/error")
    public String errorPage(){
        return "error";
    }


}


// create a platform where people can check the average prices for stuffs