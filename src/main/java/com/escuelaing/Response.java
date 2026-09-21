package com.escuelaing;

public class Response {
    int status = 20001;

    public void setStatus(int code){
        this.status = code;
    }

    public int getStatus(){
        return this.status;
    }
}
