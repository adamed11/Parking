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
public class ParkingSpot {

    private  LatLng latlng;
    private  String userName;
    private  String name;// name of parking spot
    private static ParkingSpot parkingSpot;

    private ParkingSpot() {

    }

    public ParkingSpot(double latitude, double longitude, String userName, String name) {
        latlng=new LatLng(latitude,longitude);
        this.userName=userName;
        this.name=name;
    }

    public static ParkingSpot setPointByAddress(Context context, String strAddress) {
        Log.d("PS","setPointByAddress");
        Geocoder coder = new Geocoder(context);
        //list of possible addresses
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            parkingSpot=new ParkingSpot(location.getLatitude(), location.getLongitude(),"",location.getLocality());

            if (Model.getInstance(context).addParkingSpot(parkingSpot)) {
                //adding parking spot to parse succeeded
                return parkingSpot;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public void setCameraOnPoint(GoogleMap map){
        CameraPosition BONDI =
                new CameraPosition.Builder()
                        .target(latlng)
                        .zoom(15.5f)
                        .build();
        map. moveCamera(CameraUpdateFactory
                .newCameraPosition(BONDI));
    }

    public void setMarker (GoogleMap map){
        map.addMarker(new MarkerOptions()
                .position(latlng)
                .title(name));
    }

    public Double getLat() {
        return latlng.latitude;
    }

    public Double getLong() {
        return latlng.longitude;
    }

    public String getUsername() {
        return userName;
    }

    public String getName() {
        return name;
    }
}
