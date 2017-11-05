package com.tony_justin.mobile_app.assassins;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tony_justin.mobile_app.assassin.R;

import java.util.ArrayList;

/**
 * Created by Tony Nguyen on 11/4/2017.
 */

public class CurrentGameActivity extends AppCompatActivity {

    private static final String TAG = "CurrentGameActivity";


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    String userID;
    String otherUserID;
    ArrayList<PlayerInfo> playerInfoArray = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_game);

        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        if(userID.equals("Ix7757FCsyXDuXXVV99nRtPf89C3")) {
            otherUserID = "yRY0cXSmgsNNqTPAOWoG9mecjh92";
        } else {
            otherUserID = "Ix7757FCsyXDuXXVV99nRtPf89C3";
        }
        //recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot users : dataSnapshot.getChildren()) {
                    PlayerInfo playerInfo = PlayerInfo.getInstance();
//                    playerInfo.setEmail(users.child(userID).getValue(PlayerInfo.class).getEmail());
                    playerInfo.setEmail(users.child(otherUserID).child("email").getKey());

//                    playerInfo.setLatLng(users.child(userID).getValue(PlayerInfo.class).getLatLng());
                    LatLng temp = new LatLng(users.child(otherUserID).child("location").child("latitude").getValue(Double.class).doubleValue(), users.child(userID).child("location").child("longitude").getValue(Double.class).doubleValue());
//                    playerInfo.setLatLng(users.child(userID).child("location").getValue(LatLng.class).);
                    playerInfo.setLatLng(temp);

//                    playerInfo.setLegit(users.child(userID).getValue(PlayerInfo.class).getLegit());
                    playerInfo.setLegit(users.child(otherUserID).child("legit").getValue(boolean.class).booleanValue());

//                    playerInfo.setAlive(users.child(userID).child("Alive").getValue(PlayerInfo.class).getAlive());
                    
                    playerInfoArray.add(new PlayerInfo(playerInfo.getEmail(), playerInfo.getLatLng(), playerInfo.getLegit()));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read Value.");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Starting Current Game Activity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Stopping Current Game Activity");

    }



}
