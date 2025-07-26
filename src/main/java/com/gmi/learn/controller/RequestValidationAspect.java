package com.gmi.learn.controller;

import com.gmi.learn.InitializeRequests;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class RequestValidationAspect {


@Autowired
InitializeRequests initializeRequests;

    @Pointcut("execution(* com.gmi.learn.controller..*(..))")
    public void controllerMethods(){
    }

    @Around("controllerMethods()")
    public Object checkAuthentication(ProceedingJoinPoint point) throws Throwable{
        HttpSession httpSession = null;
        List<String> methodsNotAllowedForGuest = new ArrayList<>(Arrays.asList("goToProfile","getItemAjax"));
        List<String> controllersNotAllowedForGuest = new ArrayList<>(Arrays.asList("MessageController","ItemController", "SettingsController"));

        String controllerName = point.getTarget().getClass().getSimpleName();
        String methodName = point.getSignature().getName();

        if(controllerName.equals("InitializeRequests")){
            return point.proceed();
        }
        for(Object ob: point.getArgs()){
            System.out.println("ob value = " + ob);
            if(ob instanceof HttpSession){
                System.out.println("ob after = " + ob);
                httpSession = (HttpSession) ob;
                break;
            }
        }

        System.out.println("http session = " + httpSession);
        System.out.println("method name = " + methodName);
        System.out.println("!isUserLoggedIn(httpSession) = " + !isUserLoggedIn(httpSession));
        System.out.println("controller name = " + controllerName);
        if((!isUserLoggedIn(httpSession)) ){
            if(controllersNotAllowedForGuest.contains(controllerName) || methodsNotAllowedForGuest.contains(methodName)){
                return "redirect:/dashboard";
            }else {
                return point.proceed();
            }
        }else{
            return point.proceed();
        }

    }


    public Boolean isUserLoggedIn(HttpSession session){
        Boolean isFound= initializeRequests.getSessionAttributes(session);
        return isFound;
    }
}
