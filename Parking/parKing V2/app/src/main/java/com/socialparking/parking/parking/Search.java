package com.socialparking.parking.parking;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class Search extends ActionBarActivity implements SearchFormFragment.searchCallBack{

    private List<ParkingSpot> parkingSpotList=null;
    private Model model;
    private ParkingMap map;
    private LatLng israel=new LatLng(31.046051,34.851612);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d("PS", "Search onCreate");

        model=Model.getInstance(this);

        map= new ParkingMap(this,((MapFragment)getFragmentManager().findFragmentById(R.id.fSearchMap)).getMap());
        //set initial camera position
        map.moveParkingMapCamera(new ParkingSpot(israel.latitude,israel.longitude,
                MainActivity.currentUser.getUsername(),MainActivity.currentUser.getUsername()),7);
        //updates the map
        map.refreshMap("ActionSearch");
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
            map.setCameraOnPoint(searcherSpot);
            //pins the the releaser parking spot position
            map.setMarker(searcherSpot);
            //retrieving all parking spot searchers and display them on map
            map.refreshMap("ActionSearch");
        }
        else{
            //error has occurred while saving
            ParkingAlert.showAlert("parseSaveError",null,this);
        }
    }
}
