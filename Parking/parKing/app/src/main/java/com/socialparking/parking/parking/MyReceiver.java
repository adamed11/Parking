package com.socialparking.parking.parking;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.identity.intents.AddressConstants;
import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Evyatar.m on 27/02/2015.
 */
public class MyReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        String jsonData = extras.getString("com.parse.Data");
        try {
            JSONObject jObj = new JSONObject(jsonData);
            if(jObj.getString("PushType").equals("RequestForParking")) {
                //when receiving a request, we add it to my pending request list

                PendingRequestsList.pendingRequests.add("Request for parking from: " + jObj.getString("Searcher"));
                Toast.makeText(context, jObj.getString("Searcher"), Toast.LENGTH_LONG).show();
                if(PendingRequestsList.active){
                    PendingRequestsList.adapter.notifyDataSetChanged();
                }else{
                    Intent i = new Intent(context, PendingRequestsList.class);
                    //needed to open the list activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //open the list activity to show requests
                    context.startActivity(i);
                }

            }else if(jObj.getString("PushType").equals("AnswerForRequest")){
                Toast.makeText(context, "This is an answer", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {

    }
}
