package com.gmi.learn.controller;

import com.gmi.learn.SessionUtility;
import com.gmi.learn.dao.impl.FoodDaoImpl;
import com.gmi.learn.dao.impl.UserInfoDaoImpl;
import com.gmi.learn.domain.Food;
import com.gmi.learn.domain.UserInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Controller
public class MainController {


    File file=new File("src\\main\\resources\\templates\\dashboard.html");

//    @Autowired
//    private BookDaoImpl underTest;

    @Autowired
    private UserInfoDaoImpl userInfoDao;

    @Autowired
    private FoodDaoImpl foodDao;

//    private FoodController foodController=new FoodController();

//    public HelloWorldController(UserInfoDaoImpl userInfoDao){
//        this.userInfoDao=userInfoDao;
//    }



//    @Autowired
//    private final JdbcTemplate jdbcTemplate;




    //    byte[] content = Files.readAllBytes(name);
    @GetMapping(path = "/login")
    public String loginPage(Model model, HttpSession httpSession){

        System.out.println("userid login = " + SessionUtility.getSessionValue(httpSession,"userId"));
        if(SessionUtility.getSessionValue(httpSession,"userId")!=null){
            model.addAttribute("name", httpSession.getAttribute("username"));  // You can pass more model attributes if necessary
            return "dashboard";

        }
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
        @RequestMapping(value = "/dashboard", method = {RequestMethod.GET, RequestMethod.POST})
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
//        List<Food> foodList=foodDao.fetchWithSortOrder(name,sort,order);
        List<Food> foodList=new ArrayList<>();
//        System.out.println("food list = " + foodList);
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


    @GetMapping(path = "/profile")
    public String goToProfile(HttpSession httpSession, Model model){
        String templateToRender="profile.html";
        model.addAttribute("id",SessionUtility.getSessionValue(httpSession,"userId"));
        model.addAttribute("username",SessionUtility.getSessionValue(httpSession,"username"));
        model.addAttribute("templateToRender",templateToRender);
        return "profileGeneric";
    }

    @GetMapping(path = "/logout")
    public String logoutUser(HttpSession httpSession){
        httpSession.invalidate();
        return "loginPage";
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