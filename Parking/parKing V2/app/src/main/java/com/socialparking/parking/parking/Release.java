package com.socialparking.parking.parking;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        Log.d("PS", "Release onCreate");
        model=Model.getInstance(this);

        map= new ParkingMap(this,((MapFragment)getFragmentManager().findFragmentById(R.id.fReleaseMap)).getMap());
        //set initial camera position
        map.moveParkingMapCamera(new ParkingSpot(israel.latitude,israel.longitude,
                MainActivity.currentUser.getUsername(),MainActivity.currentUser.getUsername()),7);
        //updates the map
        map.refreshMap("ActionRelease");
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
            map.setCameraOnPoint(parkingSpot);
            //pins the the releaser parking spot position
            map.setMarker(parkingSpot);
            //retrieving all parking spot searchers and display them on map
            map.refreshMap("ActionRelease");
        }else{
            //error has occurred while saving
            ParkingAlert.showAlert("parseSaveError",null,this);
        }
    }

}
