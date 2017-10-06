package com.tony_justin.mobile_app.assassins;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tony_justin.mobile_app.assassin.R;

/**
 * Created by Tony Nguyen on 10/5/2017.
 */

public class CurrentGameStatsFragment extends Fragment {

    private TextView testText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_game_stats_fragment,container,false);
        testText = (TextView) view.findViewById(R.id.testText);


        return view;
    }
}
