package com.jw.httpserver;

import java.util.HashMap;
import java.util.Map;

public class user_map {
    public Map<String, String> users = new HashMap<>();
    public static user_map instance = new user_map();
    public static user_map getInstance() {
        return instance;
    }
}
