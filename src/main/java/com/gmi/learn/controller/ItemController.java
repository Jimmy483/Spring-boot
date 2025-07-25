package com.gmi.learn.controller;

import com.gmi.learn.service.FoodService;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Autowired
    private FoodService foodService;

    @GetMapping(path="/viewItem")
    public String viewItemPage(Model model, HttpSession httpSession){
        String templateToRender="viewItems.html";
//        System.out.println("loading view page = " + name);
        Map<String, Object> dataMap=foodService.getFood(null, null, 0, "", 2, "false", true);
//        List<Map.Entry<String, String>> columnMapList = new ArrayList<>(getColumnMap().entrySet());
        model.addAttribute("templateToRender",templateToRender);
        model.addAttribute("columnMap",getColumnMap());
        model.addAttribute("data", dataMap);
        return "profileGeneric";
    }

    @PostMapping(path="/getItemFragment")
    public String getItemFragment(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size, @RequestParam(required = false) String name, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, @RequestParam(defaultValue = "false") String fromPagination){
        String templateToRender="viewItems.html";
//        System.out.println("loading view page = " + name);
        System.out.println("page = " + page + " size = " + size + " name = " + name + " sort = "+ sort + " order = " + order + " fromPagination = " + fromPagination);

        Map<String, Object> dataMap=foodService.getFood(sort, order, page, name, size, fromPagination, true);
//        List<Map.Entry<String, String>> columnMapList = new ArrayList<>(getColumnMap().entrySet());
//        model.addAttribute("templateToRender",templateToRender);
        model.addAttribute("columnMap",getColumnMap());
        model.addAttribute("data", dataMap);
        System.out.println("hi man");
        return "viewItems :: tableContent";
    }
    @GetMapping(path="/getItem")
    @ResponseBody
    public Map<String, Object> getItemAjax(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size, @RequestParam(required = false) String name, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, @RequestParam(defaultValue = "false") String fromPagination){
//        try{
//            Thread.sleep(5000);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
        if(sort.equals("")){
            sort=null;
        }
        System.out.println("page = " + page + " size = " + size + " name = " + name + " sort = "+ sort + " order = " + order + " fromPagination = " + fromPagination);

        System.out.println("this is the ajax call");
        Map<String, Object> returnMap=foodService.getFood(sort, order, page, name, size, fromPagination, true);
        returnMap.put("columnMap",getColumnMap());
        return  returnMap;
    }

    @PostMapping(path="/editDeleteRestoreFood")
    @ResponseBody
    public Map<String, Object> editDeleteOrRestoreFood(@RequestParam("id") long itemId, @RequestParam("action") String action, HttpSession httpSession){
        if(action.equals("edit")){
            return foodService.getReturnMap(itemId);
        }else{
            foodService.deleteOrRestoreFood(itemId, action, httpSession);
            return null;
        }

    }

    @PostMapping(path="/addEditItem")
    @ResponseBody
    public String saveUpdateItem(@RequestParam("name") String itemName, @RequestParam("price") int price, @RequestParam(value = "image", required = false) MultipartFile image, HttpSession httpSession, @RequestParam("action") String addOrUpdate, @RequestParam("itemId") long id){

        System.out.println("Success la");
//        String fileName='/' + image.getOriginalFilename();
        String fileName;
        if(image!=null){
            System.out.println("inside first");
            fileName = foodService.uploadImage(image);
        }else{
            fileName="";
        }

        System.out.println("fileName = " + fileName);
//        System.out.println("image name = " + fileName);
//        System.out.println("image name = " + fileName.isEmpty());
        System.out.println("id = " + id);

        if(addOrUpdate.equals("edit")){
            foodService.updateFood(itemName, price, fileName, LocalDate.now().toString(), httpSession, id);
        }else {
            foodService.saveFood(itemName, price, fileName, LocalDate.now().toString(), httpSession);
        }
        return "success";
    }

    public Map<String, String> getColumnMap(){
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("name", "Name");
        columnMap.put("price", "Price");
        columnMap.put("createdBy", "Created By");
        columnMap.put("updatedBy","Updated By");
        columnMap.put("lastUpdated", "Last Updated");
        return columnMap;
    }
}
