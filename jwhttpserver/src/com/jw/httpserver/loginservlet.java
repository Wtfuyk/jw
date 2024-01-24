package com.jw.httpserver;

import java.util.Map;

public class loginservlet extends servlet{
    @Override
    public String doRequest(String method, String url, String requestString) {
        Map<String, Object> params = null;
        if (method.equals("GET")) {
            String paramStr = url.substring(url.indexOf("?") + 1);
            params = getParams(paramStr);
        }
        if (method.equals("POST")) {
            params = getPostParams(requestString);
        }
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        if (username == null || password == null) {
            return "<hi>username or password is null</hi>";
        }
        if(!user_map.getInstance().users.containsKey(username)){
            return "<hi>username is not exist</hi>";
        }
        if(!user_map.getInstance().users.get(username).equals(password)){
            return "<hi>password is wrong</hi>";
        }
        return "<hi>Let's start surfing!</hi><script>alert('Log in success! ')</script>";
    }
}
