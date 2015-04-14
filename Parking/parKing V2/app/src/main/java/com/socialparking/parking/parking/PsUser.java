package com.socialparking.parking.parking;

import android.util.Log;

/**
 * Created by Evyatar.m on 15/02/2015.
 */
public class PsUser {

    private String username;
    private String mail;
    private String password;
    private int score;

    // sign up
    public PsUser(String username,String password,String mail){
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public PsUser(String  username,String password){
        this.username = username;
        this.password = password;
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
        return mail;
    }
}
