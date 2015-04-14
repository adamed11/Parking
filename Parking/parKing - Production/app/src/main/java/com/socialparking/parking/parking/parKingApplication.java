package com.socialparking.parking.parking;

import android.app.Application;


import com.parse.Parse;
import com.parse.PushService;


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

    }
}
