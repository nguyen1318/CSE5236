package com.tony_justin.mobile_app.assassins;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
    String userEmail;
    ArrayList<PlayerInfo> playerInfoArray = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_game);

        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        //recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot users : dataSnapshot.getChildren()) {
                    userID = users.getValue().toString();
                    PlayerInfo playerInfo = PlayerInfo.getInstance();
                    playerInfo.setEmail(users.child(userID).child("Email").getValue(PlayerInfo.class).getEmail().replace(",","."));
                    playerInfo.setLatLng(users.child(userID).child("Location").getValue(PlayerInfo.class).getLatLng());
                    playerInfo.setLegit(users.child(userID).child("Legit").getValue(PlayerInfo.class).getLegit());
                    //playerInfo.setAlive(ds.child(userID).child("Alive").getValue(PlayerInfo.class).getAlive());
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
