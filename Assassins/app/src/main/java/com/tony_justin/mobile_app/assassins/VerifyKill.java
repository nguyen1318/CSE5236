package com.tony_justin.mobile_app.assassins;

import java.util.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.location.Location;
import android.util.Log;


/**
 * Created by Justin Monte on 10/23/17.
 */

public class VerifyKill {


    String userID;
    String otherUserID;
    double lat1;
    double lat2;
    double lng1;
    double lng2;
    boolean verified;
    float distance;

    private static final String TAG = "VerifyKill";

    public boolean checkDistance() {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mRef;
        FirebaseAuth mAuth;
        /*
         * Get latitude and longitude from each player
         */

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        if(userID.equals("Ix7757FCsyXDuXXVV99nRtPf89C3")) {
            otherUserID = "yRY0cXSmgsNNqTPAOWoG9mecjh92";
        } else {
            otherUserID = "Ix7757FCsyXDuXXVV99nRtPf89C3";
        }

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot users : dataSnapshot.getChildren()) {

                        PlayerInfo playerInfo = PlayerInfo.getInstance();

                        //set and get the data
                        playerInfo.setLegit(users.child(otherUserID).child("legit").getValue(boolean.class));
                        boolean l1 = playerInfo.getLegit();
                        playerInfo.setLegit(users.child(userID).child("legit").getValue(boolean.class));
                        boolean l2 = playerInfo.getLegit();

                        lat1 = users.child(otherUserID).child("location").child("latitude").getValue(Double.class);
                        lng1 = users.child(otherUserID).child("location").child("longitude").getValue(Double.class);
                        lat2 = users.child(userID).child("location").child("latitude").getValue(Double.class);
                        lng2 = users.child(userID).child("location").child("longitude").getValue(Double.class);


                    final double KILL_RANGE = 0.00568182; // this number corresponds to 30 feet in miles (1 foot = 0.000189394 miles)

                    // lat1 and lng1 are the values of a previously stored location

                    Location loc1 = new Location("");
                    loc1.setLatitude(lat1);
                    loc1.setLongitude(lng1);

                    Location loc2 = new Location("");
                    loc2.setLatitude(lat2);
                    loc2.setLongitude(lng2);
                    distance = loc1.distanceTo(loc2);


                    if ((distance < KILL_RANGE) && l1 && l2) { // if true, we take locations as within designated range with both users in the zone
                        verified = true;
                    } else {
                        verified = false;
                    }

                    Log.d(TAG, "Verified1: " + verified);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read Value.");
            }
        });

        Log.d(TAG, "Verified2: " + verified);


        return verified;
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
