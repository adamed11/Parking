package com.socialparking.parking.parking;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.l;

import java.util.List;

/**
 * Created by Evyatar.m on 07/02/2015.
 */
public class ParkingSpot{

    private static LatLng latlng;
    private String userName;

    private String name;// name of parking spot

    public ParkingSpot(String userName) {
        this.userName=userName;
        this.name=userName;
    }

    public ParkingSpot(double latitude, double longitude, String userName, String name) {
        latlng=new LatLng(latitude,longitude);
        this.userName=userName;
        this.name=name;
    }

    public String getName() {
        //Log.d("PS", "ParkingSpot getName");
        return name;
    }

    public String getUserName(){
        Log.d("PS", "ParkingSpot getUserName");
        return userName;
    }

    public ParkingSpot convertAddressToPoint(Context context, String strAddress) {
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
