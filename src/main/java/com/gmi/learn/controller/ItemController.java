package com.gmi.learn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ItemController {

    @GetMapping(path="/item")
    public String loadItemPage(){
        System.out.println("loading page");
        return "addItem";
    }

    @GetMapping(path="/viewItem")
    public String viewItemPage(){
        System.out.println("loading view page");
        return "viewItems";
    }
    @GetMapping(path="/addItem")
    public String saveItem(){
        System.out.println("Success la");

        return "addItem";
    }
}
