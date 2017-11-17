package com.tony_justin.mobile_app.assassins;

import android.Manifest;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tony_justin.mobile_app.assassin.R;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "MapsActivity";

    float[] distance = new float[2];
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Circle playZone;
    Circle safeZoneThompson;
    Circle safeZoneUnion;
    Circle safeZoneRPAC;

    public LatLng latLng;
    public boolean outOfBounds = true;
    public boolean safeZone = false;
    public boolean legit = false;

    DatabaseReference mPlay = FirebaseDatabase.getInstance().getReference();

    PlayerInfo g = PlayerInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.d(getClass().getSimpleName(), "Map Activity Created");
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

    }


    @Override
    public void onPause() {
        super.onPause();

        Log.d(getClass().getSimpleName(), "Map Activity Paused, location removed from map");

        // Stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        Log.d(getClass().getSimpleName(), "Map Activity Ready to Connect");

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);

        }

        // Different Zones--------------------------------------------------------------------------

        playZone = mGoogleMap.addCircle(new CircleOptions()
                .center(new LatLng(39.999194, -83.014961))
                .radius(600)
                .strokeColor(Color.GREEN)
                .fillColor(0x220000FF)
                .strokeWidth(5));

        safeZoneThompson = mGoogleMap.addCircle(new CircleOptions()
                .center(new LatLng(39.999277, -83.014845))
                .radius(45)
                .strokeColor(Color.YELLOW)
                .fillColor(0x220000FF)
                .strokeWidth(5));

        safeZoneUnion = mGoogleMap.addCircle(new CircleOptions()
                .center(new LatLng(39.997637, -83.008419))
                .radius(50)
                .strokeColor(Color.YELLOW)
                .fillColor(0x220000FF)
                .strokeWidth(5));

        safeZoneRPAC = mGoogleMap.addCircle(new CircleOptions()
                .center(new LatLng(39.999441, -83.018289))
                .radius(55)
                .strokeColor(Color.YELLOW)
                .fillColor(0x220000FF)
                .strokeWidth(5));

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        Log.d(getClass().getSimpleName(), "Map Activity Connected");

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(30*1000);
        mLocationRequest.setFastestInterval(10*1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        Log.d(getClass().getSimpleName(), "Location Changed and Updated");



        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));

        // Check if in  play zone
        location.distanceBetween(location.getLatitude(), location.getLongitude(),
                playZone.getCenter().latitude, playZone.getCenter().longitude, distance);

        if( distance[0] > playZone.getRadius()  ){
            Toast.makeText(getBaseContext(), "Outside of playzone!", Toast.LENGTH_LONG).show();
            outOfBounds = true;
        } else {
            Toast.makeText(getBaseContext(), "You're inside the Play Zone! Good Luck Assassin!", Toast.LENGTH_LONG).show();
            outOfBounds = false;
        }

        // Safe zone for RPAC
        location.distanceBetween(location.getLatitude(), location.getLongitude(),
                safeZoneRPAC.getCenter().latitude, safeZoneRPAC.getCenter().longitude, distance);

        if( distance[0] <= safeZoneRPAC.getRadius()  ){
            Toast.makeText(getBaseContext(), "You're Safe! For the time being...", Toast.LENGTH_LONG).show();
            safeZone = true;
        } else {
            safeZone = false;
        }

        // Safe zone for Thompson
        location.distanceBetween(location.getLatitude(), location.getLongitude(),
                safeZoneThompson.getCenter().latitude, safeZoneThompson.getCenter().longitude, distance);

        if( distance[0] <= safeZoneThompson.getRadius()  ){
            Toast.makeText(getBaseContext(), "You're Safe! For the time being...", Toast.LENGTH_LONG).show();
            safeZone = true;
        } else {
            safeZone = false;
        }

        // Safe zone for Union
        location.distanceBetween(location.getLatitude(), location.getLongitude(),
                safeZoneUnion.getCenter().latitude, safeZoneUnion.getCenter().longitude, distance);

        if( distance[0] <= safeZoneUnion.getRadius()  ){
            Toast.makeText(getBaseContext(), "You're Safe! For the time being...", Toast.LENGTH_LONG).show();
            safeZone = true;
        } else {
            safeZone = false;
        }

        if(!safeZone){
            Toast.makeText(getBaseContext(), "Outside of safe zone!", Toast.LENGTH_LONG).show();
        }

        if(!safeZone && !outOfBounds){
            legit = true;
        } else {
            legit = false;
        }

        mPlay.child("Users").child(g.getuserid()).child("location").setValue(latLng);
        mPlay.child("Users").child(g.getuserid()).child("legit").setValue(legit);

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

}