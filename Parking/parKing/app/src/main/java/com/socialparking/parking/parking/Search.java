package com.socialparking.parking.parking;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class Search extends ActionBarActivity implements SearchFormFragment.searchCallBack{

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
        setContentView(R.layout.activity_search);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.d("PS", "Search onCreate");



        model=Model.getInstance(this);
        map= new ParkingMap(this,((MapFragment)getFragmentManager().findFragmentById(R.id.fSearchMap)).getMap());

//        //set initial camera position
//        map.moveParkingMapCamera(new ParkingSpot(israel.latitude,israel.longitude,
//                user.getUsername(),user.getUsername()),7);
        map.locateSearcher(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    public void onSearchCallBack(SearcherSpot searcherSpot) {
        Log.d("PS", "Search onSearchCallBack");
        if(model.addSearcherSpot(searcherSpot)) {
            //sets camera on releaser parking spot position
            map.setCameraOnSearcherSpot(searcherSpot);
            //pins the the releaser parking spot position
            map.setSearcherSpotMarker(searcherSpot);
        }
        else{
            //error has occurred while saving
            ParkingAlert.showAlert("parseSaveError",null,this);
        }
    }

    // serviceConnerction is an interface that must be implemented when using bound service
    private ServiceConnection sConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("PS", "Search onServiceConnected");
            DataUpdateService.LocalBinder binder=(DataUpdateService.LocalBinder)service;
            dbuService=binder.getService();
            dbuService.MapUpdateFromService(Search.this,map,"ActionSearch");
            dbuBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("PS", "Search onServiceDisconnected");
            dbuBound=false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to DataUpdateService
        Log.d("PS", "Search onStart");
        Intent intent= new Intent(this,DataUpdateService.class);
        bindService(intent,sConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        Log.d("PS", "Search onStop");
        if(dbuBound){
            unbindService(sConnection);
            dbuBound=false;
        }
    }
}
