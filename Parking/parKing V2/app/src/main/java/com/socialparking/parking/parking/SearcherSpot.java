package com.socialparking.parking.parking;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Evyatar.m on 21/02/2015.
 */
public class SearcherSpot extends Spot {

    public SearcherSpot (String userName){
        this.userName=userName;
    }

    public SearcherSpot(double latitude, double longitude, String userName){
        latlng=new LatLng(latitude,longitude);
        this.userName=userName;
    }
}
