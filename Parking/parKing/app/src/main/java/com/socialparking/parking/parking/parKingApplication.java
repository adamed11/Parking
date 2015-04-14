package com.socialparking.parking.parking;

import android.app.Application;
import android.util.Log;


import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;


/**
 * Created by Evyatar.m on 28/03/2015.
 */
public class parKingApplication extends Application {

    private String applicationID = "7vihNeuDIU6tOTxHxLxEbp9OIiV3ye7BB6cgS644";
    private String clientKey = "Rbs8OfqHyOVMu3I19KtRubYXVQp3qjoeQuKGIluz";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, applicationID,clientKey);
        ParsePush.subscribeInBackground("Adam", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel");
                }else{
                    Log.e("com.parse.push", "failed to subscribe for push",e);
                }
            }
        });
        //ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}
