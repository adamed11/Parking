package com.socialparking.parking.parking;

import android.content.Context;
import android.util.Log;

/**
 * Created by Evyatar.m on 15/02/2015.
 */
public class PsUser {

    private static PsUser instance;
    private String username;
    private String email;
    private String password;

    private PsUser(){
        username=null;
        password=null;
        email=null;
    }

    public  static PsUser getInstance(){
        if(instance==null){
            instance=new PsUser();
        }
        return instance;
    }

    public void setUsername(String username) {
        Log.d("PS","PsUser setUsername");
        this.username=username;
    }

    public void setPassword(String password) {
        Log.d("PS","PsUser setPassword");
        this.password=password;
    }

    public void setEmail(String email) {
        Log.d("PS","PsUser setEmail");
        this.email=email;

    }

    public String getUsername() {
        Log.d("PS","PsUser getUsername");
        return username;
    }

    public String getPassword() {
        Log.d("PS","PsUser getPassword");
        return password;
    }

    public String getEmail() {
        Log.d("PS","PsUser getEmail");
        return email;
    }
}
