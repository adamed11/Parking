package com.socialparking.parking.parking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.parse.ParseException;

/**
 * Created by Evyatar.m on 25/02/2015.
 */
public class ParkingAlert {

    private ParkingAlert(){

    }

    //shows alert dialog if signUp or logIn failed
    public static void showAlert(String action,ParseException e,Context context) {
        Log.d("PS", "EntryScreen showAlert");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog aD;

        if (action.equals("SignUp")) {
            builder.setMessage("Oops.. Username already exists").setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            aD = builder.create();
            aD.show();
        } else if (action.equals("LogIn")) {
            builder.setMessage("Invalid username or password!").setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            aD = builder.create();
            aD.show();
        }else if(action.equals("exception")){
            builder.setMessage(e.getMessage()).setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            aD = builder.create();
            aD.show();
        }else if(action.equals("invalidSignUp")){
            builder.setMessage("Username and Password are required!").setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            aD = builder.create();
            aD.show();
        }else if(action.equals("parseSaveError")){
            builder.setMessage("Error has occurred, try again").setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            aD = builder.create();
            aD.show();
        }else if(action.equals("addressError")){
            builder.setMessage("Address wasn't found..").setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        aD = builder.create();
        aD.show();
        }

    }
}
