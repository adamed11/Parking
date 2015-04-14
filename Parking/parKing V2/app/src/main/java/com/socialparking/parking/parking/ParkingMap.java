package com.socialparking.parking.parking;

import android.app.FragmentManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

    public ParkingMap(Context context, GoogleMap map) {
        model = Model.getInstance(context);
        this.map = map;
    }

    public static ParkingSpot setPointOnMap(Context context, ParkingSpot ps) {
        Log.d("PS", "ParkingMap setPointOnMap");

        if (Model.getInstance(context).addParkingSpot(parkingSpot)) {
            //adding parking spot to parse succeeded
            return parkingSpot;
        }
        return null;
    }

    public void setCameraOnPoint(Spot s) {
        Log.d("PS", "ParkingMap setCameraOnPoint");
        CameraPosition BONDI =
                new CameraPosition.Builder()
                        .target(s.getLatLng())
                        .zoom(15.5f)
                        .build();
        map.moveCamera(CameraUpdateFactory
                .newCameraPosition(BONDI));
    }

    public void setMarker(Spot s) {
        Log.d("PS", "ParkingMap setMarker");
        if (s instanceof ParkingSpot) {
            //is a parking spot
            map.addMarker(new MarkerOptions()
                    .position(s.getLatLng())
                    .title(((ParkingSpot) s).getName()));
        } else {
            //is a searcher
            map.addMarker(new MarkerOptions()
                    .position(s.getLatLng()).title(((SearcherSpot)s).getUsername()))
                        .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
    }

    public void moveParkingMapCamera(ParkingSpot parkingSpot, int zoom) {
        Log.d("PS", "ParkingMap moveParkingMapCamera");
        map.moveCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(parkingSpot.getLatLng())
                        .zoom(zoom)
                        .build()));
    }

    public void refreshMap(String action) {
        Log.d("PS", "ParkingMap refreshMap");
        if (action.equals("ActionRelease")) {
            model.getAllSearchers(new Model.GetAllSearchersClbck() {
                @Override
                public void done(List<SearcherSpot> searcherSpots) {
                    if (searcherSpots.size() > 0) {
                        searchersList = searcherSpots;
                        placeAllSearchersOnMap();
                    }
                }
            });
        } else if (action.equals("ActionSearch")) {
            model.getAllParkingSpots(new Model.GetAllParkingSpotsClbck() {
                @Override
                public void done(List<ParkingSpot> parkingSpots) {
                    if (parkingSpots.size() > 0) {
                        parkingSpotsList = parkingSpots;
                        placeAllParkingSpotsOnMap();
                    }
                }
            });
        }
    }

    private void placeAllSearchersOnMap() {
        Log.d("PS", "ParkingMap placeAllParkingSpotsOnMap");
        Iterator<SearcherSpot> it = searchersList.iterator();
        while (it.hasNext()) {
            SearcherSpot ss = it.next();
            setMarker(ss);
        }
    }

    private void placeAllParkingSpotsOnMap() {
        Log.d("PS", "ParkingMap placeAllParkingSpotsOnMap");
        Iterator<ParkingSpot> it = parkingSpotsList.iterator();
        while (it.hasNext()) {
            ParkingSpot ps = it.next();
            setMarker(ps);
        }
    }
}
