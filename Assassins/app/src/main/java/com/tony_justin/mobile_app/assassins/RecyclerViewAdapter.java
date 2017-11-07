package com.tony_justin.mobile_app.assassins;

/**
 * Created by Tony Nguyen on 11/4/2017.
 */

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tony_justin.mobile_app.assassin.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private final Context context;

    private String userID;
    private String otherUserID;
    private double lat1;
    private double lat2;
    private double lng1;
    private double lng2;
    private float distance;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userEmail;
//        public TextView userLocation;
        public TextView userLegit;
        public Button killButton;


        public ViewHolder(final View itemView) {
            super(itemView);
            userEmail = (TextView) itemView.findViewById(R.id.textUserEmail);
//            userLocation = (TextView) itemView.findViewById(R.id.textUserLocation);
            userLegit = (TextView) itemView.findViewById(R.id.textUserLegit);
            killButton = (Button) itemView.findViewById(R.id.killButton);

        }

    }
    private List<PlayerInfo> mPlayerInfo;

    public RecyclerViewAdapter(Context context, List<PlayerInfo> playerInfo) {
        mPlayerInfo = playerInfo;
        this.context = context;
    }


    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.users_recycler_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        PlayerInfo playerInfo = mPlayerInfo.get(position);

        Log.d(TAG, "Setting Recycler List View");

        TextView mUserEmail = viewHolder.userEmail;
        mUserEmail.setText(playerInfo.getEmail());

//        TextView mUserLocation = viewHolder.userLocation;
//        mUserLocation.setText(playerInfo.getLatLng().toString());

        TextView mUserLegit = viewHolder.userLegit;
        final Button mKillButton = viewHolder.killButton;

        if(playerInfo.getLegit()){
            mUserLegit.setText("Legit: True");
        } else {
            mUserLegit.setText("Legit: False");
        }

        /**
         * Created by Justin Monte.
         */
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
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

                    final PlayerInfo playerInfo = PlayerInfo.getInstance();

                    //set and get the data
                    playerInfo.setLegit(users.child(otherUserID).child("legit").getValue(boolean.class));
                    boolean l1 = playerInfo.getLegit();
                    playerInfo.setLegit(users.child(userID).child("legit").getValue(boolean.class));
                    boolean l2 = playerInfo.getLegit();
                    playerInfo.setLegit(users.child(userID).child("legit").getValue(boolean.class));

                    boolean a1 = users.child(otherUserID).child("Alive").getValue(boolean.class);
                    boolean a2 = users.child(userID).child("Alive").getValue(boolean.class);

                    lat1 = users.child(otherUserID).child("location").child("latitude").getValue(Double.class);
                    lng1 = users.child(otherUserID).child("location").child("longitude").getValue(Double.class);
                    lat2 = users.child(userID).child("location").child("latitude").getValue(Double.class);
                    lng2 = users.child(userID).child("location").child("longitude").getValue(Double.class);


                    final double KILL_RANGE = 10; // in meters

                    Location loc1 = new Location("");
                    loc1.setLatitude(lat1);
                    loc1.setLongitude(lng1);

                    Location loc2 = new Location("");
                    loc2.setLatitude(lat2);
                    loc2.setLongitude(lng2);
                    distance = loc1.distanceTo(loc2);


                    if ((distance < KILL_RANGE) && l1 && l2 && a1 && a2) { // if true, we take locations as within designated range with both users in the zone
                        mKillButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "Target Eliminated", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Target Eliminated");

                                mRef.child("Users").child(otherUserID).child("Alive").setValue(false);

                            }
                        });
                    } else {
                        mKillButton.setAlpha(.5f);
                        mKillButton.setClickable(false);
                        mKillButton.setText("Disabled");
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
    public int getItemCount() {
        return mPlayerInfo.size();
    }
}
