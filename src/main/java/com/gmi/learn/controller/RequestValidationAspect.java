package com.gmi.learn.controller;

import com.gmi.learn.InitializeRequests;
import com.gmi.learn.SessionUtility;
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
        List<String> methodsNotAllowedForGuest = new ArrayList<>(Arrays.asList("goToProfile","getItemAjax","roleAssign", "goToPasswordForm", "changePassword", "viewItemPage"));
        List<String> controllersNotAllowedForGuest = new ArrayList<>(Arrays.asList("MessageController","ItemController", "SettingsController"));
        List<String> methodsNotAllowedForModerator = new ArrayList<>(Arrays.asList("loadRequestForm","roleAssign","applyRoleForUser"));
        List<String> methodsNotAllowedForAssistant = new ArrayList<>(Arrays.asList("getItemAjax","roleAssign", "loadRequestForm","roleAssign","applyRoleForUser", "viewItemPage"));
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

        if(Arrays.asList("loginPage","logoutUser").contains(methodName) && SessionUtility.getSessionValue(httpSession, "userId")!=null){
            return "redirect:/dashboard";
        }

        System.out.println("method name = " + methodName);
        System.out.println("!isUserLoggedIn(httpSession) = " + !isUserLoggedIn(httpSession));
        System.out.println("controller name = " + controllerName);
        if((!isUserLoggedIn(httpSession)) || SessionUtility.getSessionValue(httpSession,"userRole").toString().isEmpty() ){
            System.out.println("first check");
            if(controllersNotAllowedForGuest.contains(controllerName) || methodsNotAllowedForGuest.contains(methodName)){
                return "redirect:/dashboard";
            }
            return point.proceed();
        }else{
            System.out.println("last check = " +SessionUtility.getSessionValue(httpSession,"userRole").toString().equals("Moderator"));
            System.out.println("last check check = " +SessionUtility.getSessionValue(httpSession,"userRole"));
            if((methodsNotAllowedForModerator.contains(methodName) && SessionUtility.getSessionValue(httpSession,"userRole").toString().equals("Moderator")) ||
                    (methodsNotAllowedForAssistant.contains(methodName) && SessionUtility.getSessionValue(httpSession, "userRole").toString().equals("Assistant"))){
                System.out.println("last inside check");
                return "redirect:/dashboard";
            }
            return point.proceed();
        }

    }


    public Boolean isUserLoggedIn(HttpSession session){
        return initializeRequests.getSessionAttributes(session);
    }
}
