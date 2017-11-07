package com.tony_justin.mobile_app.assassins;

import java.util.*;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by Justin Monte on 10/23/17.
 */

public class VerifyKill {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    private String otherUserID;
    double lat1;
    double lat2;
    double lng1;
    double lng2;
    public boolean verified;

    public boolean checkDistance() {

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
        final ArrayList<Double> LatArray = new ArrayList<Double>();
        final ArrayList<Double> LngArray = new ArrayList<Double>();
        final ArrayList<Boolean> inBoundsArray = new ArrayList<Boolean>();


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot users : dataSnapshot.getChildren()) {

                        PlayerInfo playerInfo = PlayerInfo.getInstance();

                        //set and get the data
                        playerInfo.setLegit(users.child(otherUserID).child("legit").getValue(boolean.class));
                        inBoundsArray.add(playerInfo.getLegit());
                        playerInfo.setLegit(users.child(userID).child("legit").getValue(boolean.class));
                        inBoundsArray.add(playerInfo.getLegit());
                        //playerInfo.setLatLng(users.getValue(PlayerInfo.class).getLatLng());


                        //LatArray.add(playerInfo.getLatLng().latitude);
                        //LngArray.add(playerInfo.getLatLng().longitude);
                        lat1 = users.child(otherUserID).child("location").child("latitude").getValue(Double.class);
                        lng1 = users.child(otherUserID).child("location").child("longitude").getValue(Double.class);
                        lat2 = users.child(userID).child("location").child("latitude").getValue(Double.class);
                        lng2 = users.child(userID).child("location").child("longitude").getValue(Double.class);


                    final double KILL_RANGE = 0.00568182; // this number corresponds to 30 feet in miles (1 foot = 0.000189394 miles)

                    // lat1 and lng1 are the values of a previously stored location
                    if ((distance(lat1, lng1, lat2, lng2) < KILL_RANGE) && inBoundsArray.get(0) && inBoundsArray.get(1) ) { // if true, we take locations as within designated range with both users in the zone
                        verified = true;
                    } else {
                        verified = false;
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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
