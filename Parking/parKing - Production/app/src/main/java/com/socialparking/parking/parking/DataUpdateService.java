package com.socialparking.parking.parking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Evyatar.m on 26/02/2015.
 */
public class DataUpdateService extends Service {

    Timer myTimer = new Timer();
    MyTimerTask myTask = new MyTimerTask();
    ParkingMap map;
    String action;
    Context fromContext;

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("PS", "DataUpdateService onStartCommand");
        return Service.START_NOT_STICKY;
    }

    @Override
    // return thee instance of  the local binder to the activity
    public IBinder onBind(Intent intent) {
        Log.d("PS", "DataUpdateService onBind");
        return mBinder;
    }

    public class LocalBinder extends Binder {
        DataUpdateService getService() {
            Log.d("PS", "DataUpdateService LocalBinder onBind");
            return DataUpdateService.this;
        }
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Log.d("PS", "DataUpdateService MyTimerTask run");
            map.refreshMap(action);
        }
    }

    // called from the activity
    public void MapUpdateFromService(Context context, ParkingMap map, final String action) {
        Log.d("PS", "DataUpdateService MapUpdateFromService");
        this.map = map;
        this.action = action;

        // this command activate the run function from the inner class MyTimerTask every 5 seconds.
        myTimer.schedule(myTask,0,5000);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // cancel the scheduler.
        myTimer.cancel();
    }
}
