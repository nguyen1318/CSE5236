package com.tony_justin.mobile_app.assassins;

/**
 * Created by Tony Nguyen on 11/4/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tony_justin.mobile_app.assassin.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private final Context context;

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
        Button mKillButton = viewHolder.killButton;
        VerifyKill verifyKill = new VerifyKill();
        boolean verified;
        verified = verifyKill.checkDistance();

        if(verified){
            mUserLegit.setText("Legit: True");
            mKillButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Target Eliminated", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Target Eliminated");

                    //Use Verify Kill code/class here
                    //Or disable the button underneath, or make a nested if statement to include distance
                    //Remember to change "legit" to false after killing
                }
            });
        } else {
            mUserLegit.setText("Legit: False");
            mKillButton.setAlpha(.5f);
            mKillButton.setClickable(false);
            mKillButton.setText("Disabled");
        }
    }

    @Override
    public int getItemCount() {
        return mPlayerInfo.size();
    }
}
