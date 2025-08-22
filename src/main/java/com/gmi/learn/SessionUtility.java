package com.gmi.learn;

import jakarta.servlet.http.HttpSession;

public class SessionUtility {

    public static void storeSessionValue(HttpSession session, String key, Object value) {
        session.setAttribute(key, value);
    }

    public static Object getSessionValue(HttpSession session, String key) {
        return session.getAttribute(key);
    }
}
