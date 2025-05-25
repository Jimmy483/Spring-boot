package com.gmi.learn.controller;

import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Aspect
@Component
public class RequestValidationAspect {


@Autowired
InitalizeRequests initalizeRequests;
    @Pointcut("execution(* com.gmi.learn..*(..))")
    public void applicationMethods(){
    }

    @Before("applicationMethods()")
    public void checkBefore(HttpSession session){
        System.out.println("checking before");
        if(!isUserLoggedIn(session)){
            System.out.println("before inside");
            renderLoginPage();
        }
    }

    @After("applicationMethods()")
    public void checkAfter(HttpSession session){
        System.out.println("checking after");
        if(!isUserLoggedIn(session)){
            System.out.println("inside failed case");
            renderLoginPage();
        }
    }

//    @Around("applicationMethods()")
    public Object renderLoginPage(){
        return "redirect:/loginPage";
    }


    public Boolean isUserLoggedIn(HttpSession session){
        Boolean isFound= initalizeRequests.getSessionAttributes(session);
        return isFound;
    }
}
