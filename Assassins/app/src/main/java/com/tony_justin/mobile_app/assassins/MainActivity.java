package com.tony_justin.mobile_app.assassins;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tony_justin.mobile_app.assassin.R;

/**
 * Created by Tony Nguyen on 9/29/2017.
 */

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private static final String TAG = "MainActivity";
    private static final long FASTEST_INTERVAL = 2000;
    private static final long UPDATE_INTERVAL = 10000;

    private ViewPager mViewPager;

    double lat;
    double lon;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;


    private DrawerLayout drawer;
    private NavigationView navigationView;
    Button startNewGame;
    Button currentGame;
    Button gameInvites;
    Button myStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Nav Drawer Stuff ------------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        mViewPager = (ViewPager) findViewById(R.id.container);setupViewPager(mViewPager);

        Button connectLogout = (Button)findViewById(R.id.connectLogout);
        connectLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginScreen = new Intent(MainActivity.this, LoginActivity.class);
                ComponentName cn = loginScreen.getComponent();

                Intent logOut = IntentCompat.makeRestartActivityTask(cn);

                startActivity(logOut);
            }
        });
        // Done Nav Drawer -------------------------------------------------------------------------

        //Location API -----------------------------------------------------------------------------

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //Ask for location permissions
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
            }
        }

        // Game Menu Buttons -----------------------------------------------------------------------
        startNewGame = (Button) findViewById(R.id.startNewGame);
        currentGame = (Button) findViewById(R.id.currentGame);
        gameInvites = (Button) findViewById(R.id.gameInvites);
        myStats = (Button) findViewById(R.id.myStats);

        startNewGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Make a pop-up dialog box asking if they want to start a new game?
                // It will delete their previous game since only one game is allowed at a time.
                Toast.makeText(MainActivity.this, "Starting New Game", Toast.LENGTH_SHORT).show();
            }
        });

        currentGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Bring up their current game, people in it, fragment with map, etc.
                Intent currentGameIntent = new Intent(MainActivity.this, CurrentGameActivity.class);
                MainActivity.this.startActivity(currentGameIntent);
                Toast.makeText(MainActivity.this, "Resuming Current Game", Toast.LENGTH_SHORT).show();
            }
        });

        gameInvites.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Show the game invitations they received
                Toast.makeText(MainActivity.this, "Getting Invites", Toast.LENGTH_SHORT).show();
            }
        });

        myStats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Show pedometer for the day, kills for the week, deaths for the week
                // Calories burned, distance walked
                Toast.makeText(MainActivity.this, "Showing Stats", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        //adapter.addFragment(new GameMenuFragment());
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.ic_home: {
                break;
            }
            case R.id.ic_search: {
                Intent searchIntent = new Intent(MainActivity.this, ActivitySearch.class);
                MainActivity.this.startActivity(searchIntent);
                break;
            }
            case R.id.ic_connect: {
                Intent connectIntent = new Intent(MainActivity.this, ActivityConnect.class);
                MainActivity.this.startActivity(connectIntent);
                break;
            }
            case R.id.ic_settings: {
                Intent settingsIntent = new Intent(MainActivity.this, ActivitySettings.class);
                MainActivity.this.startActivity(settingsIntent);
                break;
            }
            case R.id.ic_help: {
                Intent helpIntent = new Intent(MainActivity.this, ActivityHelp.class);
                MainActivity.this.startActivity(helpIntent);
                break;
            }
        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //Google API Client Location Functions----------------------------------------------------------
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);

            return;
        }

        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            lat = mLocation.getLatitude();
            lon = mLocation.getLongitude();
        } else {
            // Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    //End of Google API Location Service Functions -------------------------------------------------

}

