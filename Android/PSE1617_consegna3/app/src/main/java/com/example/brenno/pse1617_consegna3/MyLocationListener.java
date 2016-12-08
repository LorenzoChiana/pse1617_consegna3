package com.example.brenno.pse1617_consegna3;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

class MyLocationListener implements LocationListener {
    private double latitude;
    private double longitude;

    public void onLocationChanged(Location location) {
        latitude =  location.getLatitude();
        longitude = location.getLongitude();
        Log.d("pos", "Listen: (" + latitude + "," + longitude + ")");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onProviderDisabled(String provider) {

    }

    double getLatitude() {
        return latitude;
    }

    double getLongitude() {
        return longitude;
    }

}
