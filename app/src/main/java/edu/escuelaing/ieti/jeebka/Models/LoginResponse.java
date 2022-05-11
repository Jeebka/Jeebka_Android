package edu.escuelaing.ieti.jeebka.Models;

public class LoginResponse {
    public String email;
    public String msg;
    public String token;

    public String getEmail(){
        return  email;
    }

    public String getMsg(){
        return msg;
    }

    public String getToken(){
        return token;
    }
}
