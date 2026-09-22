package com.escuelaing;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private final Map<String, WebService> routes = new HashMap<>();

    public void add(String route, WebService service) {
        routes.put(route, service);
    }

    public WebService resolve(String path) {
        return routes.get(path);
    }
}
