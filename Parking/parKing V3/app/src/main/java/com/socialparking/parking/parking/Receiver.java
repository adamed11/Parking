package com.socialparking.parking.parking;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.identity.intents.AddressConstants;
import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Evyatar.m on 27/02/2015.
 */
public class Receiver extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        Log.d("PS", "before super Receiver onPushOpen");

        ParseAnalytics.trackAppOpenedInBackground(intent);

    }
}
