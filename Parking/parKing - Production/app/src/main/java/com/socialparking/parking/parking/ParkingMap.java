package com.socialparking.parking.parking;

import android.app.FragmentManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Evyatar.m on 21/02/2015.
 */
public class ParkingMap {

    private GoogleMap map;
    private List<ParkingSpot> parkingSpotsList;
    private List<SearcherSpot> searchersList;
    private static ParkingSpot parkingSpot;
    Model model;
    PsUser user=PsUser.getInstance();


    public ParkingMap(Context context, GoogleMap map) {
        model = Model.getInstance(context);
        this.map = map;

        setMarkersOnClik();
    }

    //respond to marker clicked by notifying parking spot user
    //we create a new record in request for parking spot table in order to know which user to notify
    private void setMarkersOnClik() {
        Log.d("PS", "ParkingMap setMarkersOnClik");
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Create our Installation query
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("myUser","adam");

                // Send push notification to query
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery); // Set our Installation query
                push.setMessage("production parking");
                push.sendInBackground();
//                ParseObject ps = new ParseObject("RequestForParking");
//                ps.put("requestor",user.getUsername());
//                ps.put("requestedUser",marker.getTitle());
//                try {
//                    ps.save();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                return false;
            }
        });
    }

//    public static ParkingSpot setPointOnMap(Context context, ParkingSpot ps) {
//        Log.d("PS", "ParkingMap setPointOnMap");
//
//        if (Model.getInstance(context).addParkingSpot(parkingSpot)) {
//            //adding parking spot to parse succeeded
//            return parkingSpot;
//        }
//        return null;
//    }

    //moves the camera to the requested spot
    public void setCameraOnParkingSpot(ParkingSpot ps) {
        Log.d("PS", "ParkingMap setCameraOnPoint");
        CameraPosition BONDI =
                new CameraPosition.Builder()
                        .target(ps.getLatLng())
                        .zoom(15.5f)
                        .build();
        map.moveCamera(CameraUpdateFactory
                .newCameraPosition(BONDI));
    }

    //moves the camera to the requested spot
    public void setCameraOnSearcherSpot(SearcherSpot ss) {
        Log.d("PS", "ParkingMap setCameraOnPoint");
        CameraPosition BONDI =
                new CameraPosition.Builder()
                        .target(ss.getLatLng())
                        .zoom(15.5f)
                        .build();
        map.moveCamera(CameraUpdateFactory
                .newCameraPosition(BONDI));
    }

    //creates a marker in the requested spot
    public void setParkingSpotMarker(ParkingSpot ps) {
        Log.d("PS", "ParkingMap setParkinSpotMarker");
        map.addMarker(new MarkerOptions()
                .position(ps.getLatLng())
                .title(ps.getName()));
    }

    //creates a marker in the requested spot
    public void setSearcherSpotMarker(SearcherSpot ss) {
        Log.d("PS", "ParkingMap setSearcherSpotMarker");
        map.addMarker(new MarkerOptions()
                .position(ss.getLatLng()).title(ss.getUsername()))
                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
    }

    public void moveParkingMapCamera(ParkingSpot parkingSpot, int zoom) {
        Log.d("PS", "ParkingMap moveParkingMapCamera");
        map.moveCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(parkingSpot.getLatLng())
                        .zoom(zoom)
                        .build()));
    }

    //updates all points on map
    public void refreshMap(String action) {
        Log.d("PS", "ParkingMap refreshMap");
//        map.clear();
        if (action.equals("ActionRelease")) {
            model.getAllSearcherSpots(new Model.GetAllSearchersClbck() {
                @Override
                public void done(List<SearcherSpot> searcherSpots) {
                    if (searcherSpots.size() > 0) {
                        searchersList = searcherSpots;
                        //placeAllSearchersOnMap();
                    }
                }
            },this);
        } else if (action.equals("ActionSearch")) {
            model.getAllParkingSpots(new Model.GetAllParkingSpotsClbck() {
                @Override
                public void done(List<ParkingSpot> parkingSpots) {
                    if (parkingSpots.size() > 0) {
                        parkingSpotsList = parkingSpots;
                        //placeAllParkingSpotsOnMap();
                    }
                }
            },this);
        }
    }

    public void locateSearcher(final Context context){

        LocationManager manager= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener mlocListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                model.editSearcherSpot(location);
                setCameraOnSearcherSpot(new SearcherSpot(location.getLatitude(),location.getLongitude(),
                        user.getUsername()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(context,
                        "GPS Enabled",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(context,
                        "GPS Disabled",
                        Toast.LENGTH_SHORT).show();
            }
        };
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mlocListener);
    }


//    //*****************************************************************************
//    //  we will use those methods if we succeed to resolve the add to list  problem
//    //*****************************************************************************
//    private void placeAllSearchersOnMap() {
//        Log.d("PS", "ParkingMap placeAllParkingSpotsOnMap");
//        Iterator<SearcherSpot> it = searchersList.iterator();
//        while (it.hasNext()) {
//            SearcherSpot ss = it.next();
//            setSearcherSpotMarker(ss);
//        }
//    }
//
//    private void placeAllParkingSpotsOnMap() {
//        Log.d("PS", "ParkingMap placeAllParkingSpotsOnMap");
//        Iterator<ParkingSpot> it = parkingSpotsList.iterator();
//        while (it.hasNext()) {
//            ParkingSpot ps = it.next();
//            setParkingSpotMarker(ps);
//        }
//    }
}
