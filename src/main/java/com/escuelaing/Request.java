package com.escuelaing;

import java.util.HashMap;
import java.util.Map;


public class Request {
    Map<String, String> body = new HashMap<>();

    public String getValue(String key){
        return body.get(key);
    }

    public void setValue(String key, String newKey){
        String actualKey = getValue(key);

        body.remove(actualKey);
        if( actualKey != null){
            body.put(newKey, actualKey);
        }
    }
}
