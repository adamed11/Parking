package com.socialparking.parking.parking;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Evyatar.m on 21/02/2015.
 */
public class Spot {
    protected static LatLng latlng;
    protected String userName;

    public Spot convertAddressToPoint(Context context, String strAddress) {
        Log.d("PS", "Spot convertAddressToPoint");
        Geocoder coder = new Geocoder(context);
        //list of possible addresses
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            latlng=new LatLng(location.getLatitude(),location.getLongitude());
            return this;
        } catch (Exception e) {
            return null;
        }
    }

//    public void setCameraOnPoint(GoogleMap map){
//        Log.d("PS", "Spot setCameraOnPoint");
//        CameraPosition BONDI =
//                new CameraPosition.Builder()
//                        .target(latlng)
//                        .zoom(15.5f)
//                        .build();
//        map. moveCamera(CameraUpdateFactory
//                .newCameraPosition(BONDI));
//    }

    public Double getLat() {
        return latlng.latitude;
    }

    public Double getLong() {
        return latlng.longitude;
    }

    public LatLng getLatLng(){
        return latlng;
    }

    public String getUsername() {
        return userName;
    }
}
