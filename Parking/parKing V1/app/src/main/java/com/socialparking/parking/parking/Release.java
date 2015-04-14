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
    private GoogleMap map;
    private LatLng israel=new LatLng(31.046051,34.851612);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        Log.d("PS", "Release onCreate");
        model=Model.getInstance(this);

        map=((MapFragment)getFragmentManager().findFragmentById(R.id.fReleaseMap)).getMap();
        //set initial camera position
        map.moveCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(israel)
                        .zoom(7)
                        .build()));
        //updates the map
        refreshMap();
    }

    @Override
    public void onBackPressed() {
        Log.d("PS","Release obBackPressed");
        super.onBackPressed();
    }

    private void refreshMap(){
        model.getAllParkingSpots(new Model.GetAllParkingSpotsClbck() {
            @Override
            public void done(List<ParkingSpot> parkingSpots) {
                parkingSpotList=parkingSpots;
                placeAllParkingSpotsOnMap(map);
            }
        });
    }

//    @Override
//    protected void onPause() {
//        Log.d("PS", "Release onPause");
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        Log.d("PS", "Release onStop");
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        Log.d("PS", "Release onDestroy");
//        super.onDestroy();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_release, menu);
        Log.d("PS", "Release onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("PS", "Release onOptionsItemSelected");
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
        //sets initial camera position
        parkingSpot.setCameraOnPoint(map);
        //retrieving all parking spots and display them on map
        refreshMap();
    }

    public void placeAllParkingSpotsOnMap(GoogleMap map){
        Log.d("PS","Release placeAllParkingSpotsOnMap");
        if(parkingSpotList!=null) {
            Iterator<ParkingSpot> it = parkingSpotList.iterator();
            while (it.hasNext()) {
                ParkingSpot ps = it.next();
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(ps.getLat(), ps.getLong()))
                        .title(ps.getName()));
            }
        }

    }

}
