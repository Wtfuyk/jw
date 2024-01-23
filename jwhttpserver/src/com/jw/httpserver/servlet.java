package com.jw.httpserver;

import java.util.HashMap;
import java.util.Map;

public abstract class servlet {
    public abstract String doRequest(String method, String url, String requestString);

    public Map<String, Object> getParams(String paramString) {
        Map<String, Object> map = new HashMap<>();
        if (paramString == null || paramString.trim().equals("")) {
            return map;
        }
        String[] params = paramString.split("&");
        for (String param : params) {
            String[] p = param.split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    public Map<String, Object> getPostParams(String requestString) {
        String[] requestArr = requestString.split("\r\n");
        String paramStr = requestArr[requestArr.length - 1];
        System.out.println(paramStr);
        return getParams(paramStr);
    }
}
