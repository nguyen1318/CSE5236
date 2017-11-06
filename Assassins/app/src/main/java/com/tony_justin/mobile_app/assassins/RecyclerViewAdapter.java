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
import android.widget.TextView;

import com.tony_justin.mobile_app.assassin.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userEmail;
        public TextView userLocation;
        public TextView userLegit;

        public ViewHolder(final View itemView) {
            super(itemView);
            userEmail = (TextView) itemView.findViewById(R.id.textUserEmail);
            userLocation = (TextView) itemView.findViewById(R.id.textUserLocation);
            userLegit = (TextView) itemView.findViewById(R.id.textUserLegit);

        }

    }
    private List<PlayerInfo> mPlayerInfo;

    public RecyclerViewAdapter(List<PlayerInfo> playerInfo) {
        mPlayerInfo = playerInfo;
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

        TextView mUserLocation = viewHolder.userLocation;
        mUserLocation.setText(playerInfo.getLatLng().toString());

        TextView mUserLegit = viewHolder.userLegit;
        if(playerInfo.getLegit()){
            mUserLegit.setText("Legit: T");
        } else {
            mUserLegit.setText("Legit: F");
        }
    }

    @Override
    public int getItemCount() {
        return mPlayerInfo.size();
    }
}
