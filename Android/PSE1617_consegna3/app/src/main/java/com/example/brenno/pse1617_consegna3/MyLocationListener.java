package com.example.brenno.pse1617_consegna3;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.brenno.pse1617_consegna3.MainActivity;

/**
 * Created by brenno on 06/12/2016.
 */

public class MyLocationListener implements LocationListener {

    private double latitude;
    private double longitude;
    private MainActivity activity;

    public MyLocationListener(MainActivity activity) {
        this.activity = activity;
    }

    public void onLocationChanged(Location location) {
        latitude =  location.getLatitude();
        longitude = location.getLongitude();
        Log.d("pos", "Listen: (" + latitude + "," + longitude + ")");
        activity.setLatitude(latitude);
        activity.setLongitude(longitude);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onProviderDisabled(String provider) {

    }
}
