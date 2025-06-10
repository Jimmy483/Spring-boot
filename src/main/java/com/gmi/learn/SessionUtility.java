package com.gmi.learn;

import jakarta.servlet.http.HttpSession;

public class SessionUtility {

    // Utility method to store a key-value pair in the session
    public static void storeSessionValue(HttpSession session, String key, Object value) {
        session.setAttribute(key, value);
    }

    // Utility method to retrieve a value from the session
    public static Object getSessionValue(HttpSession session, String key) {
//        Object value=session.getAttribute(key);
//        if(value==null){
//            throw new RuntimeException("Session Value not found for " + key );
//
//        }
        return session.getAttribute(key);
    }
}
