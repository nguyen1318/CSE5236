package com.tony_justin.mobile_app.assassins;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tony_justin.mobile_app.assassin.R;

/**
 * Created by Tony Nguyen on 10/4/2017.
 */

public class CurrentGameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_game);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MapViewFragment map = new MapViewFragment();
        fragmentTransaction.add(R.id.mapView, map);
        fragmentTransaction.commit();
    }
}
