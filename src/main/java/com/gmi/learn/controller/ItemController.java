package com.gmi.learn.controller;

import com.gmi.learn.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Autowired
    private FoodService foodService;

    @GetMapping(path="/viewItem")
    public String viewItemPage(Model model){
        String templateToRender="viewItems.html";
//        System.out.println("loading view page = " + name);
        Map<String, Object> dataMap=foodService.getFood(null, null, 0, "", 2, "false");
//        List<Map.Entry<String, String>> columnMapList = new ArrayList<>(getColumnMap().entrySet());
        model.addAttribute("templateToRender",templateToRender);
        model.addAttribute("columnMap",getColumnMap());
        model.addAttribute("data", dataMap);
        return "profileGeneric";
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
        Map<String, Object> returnMap=foodService.getFood(sort, order, page, name, size, fromPagination);
        returnMap.put("columnMap",getColumnMap());
        return  returnMap;
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
