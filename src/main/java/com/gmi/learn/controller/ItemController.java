package com.gmi.learn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ItemController {

    @GetMapping(path="/item")
    public String loadItemPage(){
        System.out.println("loading page");
        return "addItem";
    }

    @GetMapping(path="/viewItem")
    public String viewItemPage(Model model){
        String templateToRender="viewItems.html";
        System.out.println("loading view page");
        model.addAttribute("templateToRender",templateToRender);
        return "profileGeneric";
    }

}
