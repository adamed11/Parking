package com.socialparking.parking.parking;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Release extends ActionBarActivity implements ReleaseFormFragment.releaseCallBack {

    private List<ParkingSpot> parkingSpotList=null;
    private Model model;
    private ParkingMap map;
    private LatLng israel=new LatLng(31.046051,34.851612);
    DataUpdateService dbuService;
    boolean dbuBound=false;// when service connected get true
    PsUser user=PsUser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.d("PS", "Release onCreate");
        model=Model.getInstance(this);

        map= new ParkingMap(this,((MapFragment)getFragmentManager().findFragmentById(R.id.fReleaseMap)).getMap());
        //set initial camera position
        map.moveParkingMapCamera(new ParkingSpot(israel.latitude,israel.longitude,
                user.getUsername(),user.getUsername()),7);


//        // Set Notification Title
//        String strtitle = "TITLE";
//        // Set Notification Text
//        String strtext = "TEXT";
//
//        // Open NotificationView Class on Notification Click
//        Intent intent = new Intent(this, MainActivity.class);
//        // Send data to NotificationView Class
//        intent.putExtra("title", strtitle);
//        intent.putExtra("text", strtext);
//        // Open NotificationView.java Activity
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        //Create Notification using NotificationCompat.Builder
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                // Set Icon
//                .setSmallIcon(R.drawable.parking_logo)
//                        // Set Ticker Message
//                .setTicker("TICKER")
//                        // Set Title
//                .setContentTitle("CONTENT TITLE")
//                        // Set Text
//                .setContentText("NOTIFICATION TEXT")
//                        // Add an Action Button below Notification
//                .addAction(R.drawable.ic_launcher, "Action Button", pIntent)
//                        // Set PendingIntent into Notification
//                .setContentIntent(pIntent)
//                        // Dismiss Notification
//                .setAutoCancel(true);
//
//        // Create Notification Manager
//        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        // Build Notification with Notification Manager
//        notificationmanager.notify(0, builder.build());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_release, menu);
//        Log.d("PS", "Release onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.d("PS", "Release onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReleaseCallBack(ParkingSpot parkingSpot) {
        Log.d("PS","Release onReleaseCallBack");
        if(model.addParkingSpot(parkingSpot)) {
            //sets camera on releaser parking spot position
            map.setCameraOnParkingSpot(parkingSpot);
            //pins the the releaser parking spot position
            map.setParkingSpotMarker(parkingSpot);
        }else{
            //error has occurred while saving
            ParkingAlert.showAlert("parseSaveError",null,this);
        }
    }

    // serviceConnerction is an interface that must be implemented when using bound service
    private ServiceConnection sConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("PS", "Release onServiceConnected");
            DataUpdateService.LocalBinder binder=(DataUpdateService.LocalBinder)service;
            dbuService=binder.getService();
            dbuService.MapUpdateFromService(Release.this,map,"ActionRelease");
            dbuBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("PS", "Release onServiceDisconnected");
            dbuBound=false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to DataUpdateService
        Log.d("PS", "Release onStart");
        Intent intent= new Intent(this,DataUpdateService.class);
        bindService(intent,sConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        Log.d("PS", "Release onStop");
        if(dbuBound){
            unbindService(sConnection);
            dbuBound=false;
        }
    }

}
