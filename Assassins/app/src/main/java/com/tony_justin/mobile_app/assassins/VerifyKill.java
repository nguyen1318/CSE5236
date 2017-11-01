package com.tony_justin.mobile_app.assassins;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;


/**
 *
 * Source: https://stackoverflow.com/questions/2227292/how-to-get-latitude-and-longitude-of-the-mobile-device-in-android
 *
 * Created by Justin Monte on 10/23/17.
 */

public class VerifyKill {


    public boolean checkDistance() {

        /*
         * Get latitude and longitude from each player, these values are currently just placeholders before we implement
         * the rest of the code.
         */

        //LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //double longitude = location.getLongitude();
        //double latitude = location.getLatitude();
        double lat1 = 34;
        double lng1 = 32;
        double lat2 = 43;
        double lng2 = 49;
        final double KILL_RANGE = 0.00568182; // this number corresponds to 30 feet in miles (1 foot = 0.000189394 miles)

        // lat1 and lng1 are the values of a previously stored location
        if (distance(lat1, lng1, lat2, lng2) < KILL_RANGE) { // if true, we take locations as within designated range
            return true;
        } else {
            return false;
        }
    }

    /*
    * calculates the distance between two locations in MILES
    */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }


}
