package com.tony_justin.mobile_app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tony_justin.mobile_app.assassin.R;

import java.util.ArrayList;
import java.util.Calendar;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by Tony Nguyen on 9/29/2017.
 */

public class LocationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    private static final String TAG = "LocationFragment";
    private static final long FASTEST_INTERVAL = 2000;
    private static final long UPDATE_INTERVAL = 10000;

    private ViewPager mViewPager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    private FloatingActionButton button;
    private TextView textView;
    double lat;
    double lon;
    ArrayList<Card> list = new ArrayList<>();
    private String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

    //Database Test
    DatabaseHelper mDatabaseHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_fragment,container,false);

        //Database
        mDatabaseHelper = new DatabaseHelper(getActivity());

        ListView mListView = (ListView) view.findViewById(R.id.listView);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);



        //Testing Location -------------------------------------------------------
        button = (FloatingActionButton) view.findViewById(R.id.fab);
        //textView = (TextView) view.findViewById(R.id.locationText);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
                return view;
            } else {
                configureButton();
            }
        } else{
            configureButton();
        }
        addDefaultCards();
        //Populate App with cards within phone's database
        populateCards();
        CustomListAdapter adapter = new CustomListAdapter(getActivity(), R.layout.card_layout_main, list);
        mListView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 10:
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    configureButton();
                }
        }
    }

    private void addDefaultCards() {
        //Example Cards-----------------------------------------------------------------------------
        list.add(new Card("drawable://" + R.drawable.booz_allen_dc, "Booz Allen Hamilton", "38.901712", "-77.033035", mydate));
        //addToDB("drawable://" + R.drawable.booz_allen_dc, "Booz Allen Hamilton", 38.901712, -77.033035, mydate);
        list.add(new Card("drawable://" + R.drawable.capital_building, "Washington DC Capital Building", "38.890214", "-77.008997", mydate));
        //addToDB("drawable://" + R.drawable.capital_building, "Washington DC Capital Building", 38.890214, -77.008997, mydate);
        list.add(new Card("drawable://" + R.drawable.cherry_blossoms, "Jefferson Memorial", "38.881563", "-77.036457", mydate));
        //addToDB("drawable://" + R.drawable.cherry_blossoms, "Jefferson Memorial", 38.881563, -77.036457, mydate);
        list.add(new Card("drawable://" + R.drawable.ww2_memorial, "WWII Memorial", "38.889414", "-77.040477", mydate));
        //addToDB("drawable://" + R.drawable.ww2_memorial, "WWII Memorial", 38.889414, -77.040477, mydate);
        list.add(new Card("drawable://" + R.drawable.washington_capitals, "Washington Capitals Game", "N/A", "N/A", mydate));
        //addToDB("drawable://" + R.drawable.washington_capitals, "Washington Capitals Game", 38.901712, -77.033035, mydate);
        list.add(new Card("drawable://" + R.drawable.washington_nationals, "Washington Nationals Game", "N/A", "N/A", mydate));
        //addToDB("drawable://" + R.drawable.washington_nationals, "Washington Nationals Game", 38.901712, -77.033035, mydate);
        list.add(new Card("drawable://" + R.drawable.washington_wizards, "Washington Wizards Game", "N/A", "N/A", mydate));
        //addToDB("drawable://" + R.drawable.washington_wizards, "Washington Wizards Game", 38.901712, -77.033035, mydate);
        //Done Example Cards------------------------------------------------------------------------
    }
    private void configureButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                final EditText input = new EditText(getActivity());
                input.setHint("CAPTION");
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);
                alertDialog.setTitle("ADD A LOCATION");
                alertDialog.setView(input);
                alertDialog.setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(lat != 0.0 || lon != 0.0){
                                    addToDB("drawable://" + R.drawable.arizona_dessert, input.getText().toString(), lat, lon, mydate);
                                    Toast.makeText(getActivity(), "Location Captured", Toast.LENGTH_SHORT).show();
                                    populateCards();
                                } else {
                                    Toast.makeText(getActivity(), "Too close to last location", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                alertDialog.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });
    }
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);
            return;
        } startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            lat = mLocation.getLatitude();
            lon = mLocation.getLongitude();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
    }

    //Done Testing Location-------------------------------------------------------------------------

    //Database--------------------------------------------------------------------------------------
    public void addToDB(String photo, String caption, double lat, double lon, String date){
        mDatabaseHelper.addData(photo, caption, lat, lon, date);
    }

    private void populateCards(){
        Log.d(TAG, "populateCards: Displaying Cards");
        Cursor data = mDatabaseHelper.getData();
        //Get the data and populate list
        while (data.moveToNext()) {
            if (!list.contains(new Card(data.getString(1), data.getString(2) + "\n", " " + data.getDouble(3), " " + data.getDouble(4), "Time : " + data.getString(5)))) {
                list.add(new Card(data.getString(1), data.getString(2) + "\n", " " + data.getDouble(3), " " + data.getDouble(4), "Time : " + data.getString(5)));
            }
        }
    }

    //End Database----------------------------------------------------------------------------------

}
