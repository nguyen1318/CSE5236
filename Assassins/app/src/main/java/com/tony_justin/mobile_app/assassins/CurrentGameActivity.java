package com.tony_justin.mobile_app.assassins;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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
import java.util.List;

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
    List<PlayerInfo> playerInfoArray = new ArrayList<>();
    int i = 0;

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

        //recyclerView = (RecyclerView) findViewById(R.id.rv);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recyclerView = (RecyclerView) findViewById(R.id.rv);

                for(DataSnapshot users : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Getting info from Database");
                    PlayerInfo playerInfo = PlayerInfo.getInstance();
//                    playerInfo.setEmail(users.child(userID).getValue(PlayerInfo.class).getEmail());
                    playerInfo.setEmail(users.child(otherUserID).child("email").getValue(String.class));
                    Log.d(TAG, playerInfo.getEmail());

//                    playerInfo.setLatLng(users.child(userID).getValue(PlayerInfo.class).getLatLng());
                    Double tempLat = users.child(otherUserID).child("location").child("latitude").getValue(Double.class);
                    Double tempLon = users.child(otherUserID).child("location").child("longitude").getValue(Double.class);
//                    playerInfo.setLatLng(users.child(userID).child("location").getValue(LatLng.class).);

                    LatLng temp = new LatLng(tempLat, tempLon);
                    playerInfo.setLatLng(temp);
                    Log.d(TAG, playerInfo.getLatLng().toString());

//                    playerInfo.setLegit(users.child(userID).getValue(PlayerInfo.class).getLegit());
                    playerInfo.setLegit(users.child(otherUserID).child("legit").getValue(boolean.class));
                    Log.d(TAG, users.child(otherUserID).child("legit").getValue(boolean.class).toString());

//                    playerInfo.setAlive(users.child(userID).child("Alive").getValue(PlayerInfo.class).getAlive());

                    PlayerInfo tempPlayer = new PlayerInfo(playerInfo.getEmail(), playerInfo.getLatLng(), playerInfo.getLegit());
                    playerInfoArray.add(i , tempPlayer);
                    i++;
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(playerInfoArray);
                    recyclerView.setLayoutManager((new LinearLayoutManager(CurrentGameActivity.this)));
                    recyclerView.setAdapter(adapter);
                    if(playerInfoArray.size() == 0 ){
                        Toast.makeText(CurrentGameActivity.this, "No data to show!", Toast.LENGTH_SHORT);
                    }
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
