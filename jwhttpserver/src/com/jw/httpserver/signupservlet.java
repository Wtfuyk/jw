package com.jw.httpserver;

import java.util.Map;

public class signupservlet extends servlet{
    @Override
    public String doRequest(String method, String url, String requestString) {
        Map<String, Object> params = null;
        if (method.equals("POST")) {
            params = getPostParams(requestString);
        }
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        if (username == null || password == null) {
            return "<hi>username or password is null</hi>";
        }
        user_map.getInstance().users.put(username, password);
//        System.out.println(user_map.getInstance().users);
        return "<hi>Sign up success!</hi>";
    }
}