package com.escuelaing;

import java.util.HashMap;
import java.util.Map;

public class WebFramework {

    Map<String, WebService> webServices = new HashMap<>();

    public void get(String route, WebService ws) {
        webServices.put(route, ws);
    }

    public void start(){

    }

    public void staticFiles(String staticfile){

    }
    
}
