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
        if (!username.equals("admin") || !password.equals("123456")) {
            return "<hi>username or password is wrong</hi>";
        }
        return "<hi>success</hi>";
    }
}
