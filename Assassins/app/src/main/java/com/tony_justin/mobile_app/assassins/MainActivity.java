package com.tony_justin.mobile_app.assassins;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tony_justin.mobile_app.assassin.R;

/**
 * Created by Tony Nguyen on 9/29/2017.
 */

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";

    private ViewPager mViewPager;


    private DrawerLayout drawer;
    private NavigationView navigationView;
    Button startNewGame;
    Button currentGameMap;
    Button currentGame;
    Button myStats;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private String userID;
    private String otherUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Main Hub Created");

        PlayerInfo playerInfo = PlayerInfo.getInstance();

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

        // Game Menu Buttons -----------------------------------------------------------------------
        startNewGame = (Button) findViewById(R.id.startNewGame);
        currentGameMap = (Button) findViewById(R.id.currentGameMap);
        currentGame = (Button) findViewById(R.id.currentGame);
        myStats = (Button) findViewById(R.id.myStats);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        if(userID.equals("Ix7757FCsyXDuXXVV99nRtPf89C3")) {
            otherUserID = "yRY0cXSmgsNNqTPAOWoG9mecjh92";
        } else {
            otherUserID = "Ix7757FCsyXDuXXVV99nRtPf89C3";
        }

        startNewGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Make a pop-up dialog box asking if they want to start a new game?
                // It will delete their previous game since only one game is allowed at a time.
                mRef.child("Users").child(userID).child("Alive").setValue(true);
                mRef.child("Users").child(otherUserID).child("Alive").setValue(true);
                Toast.makeText(MainActivity.this, "Starting New Game: Players reset to Alive", Toast.LENGTH_SHORT).show();
            }
        });

        currentGameMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Bring up their current game, people in it, fragment with map, etc.
                Intent currentGameMapIntent = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivity(currentGameMapIntent);
                Toast.makeText(MainActivity.this, "Current Game Map Initiating", Toast.LENGTH_SHORT).show();
            }
        });

        currentGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Show the game invitations they received
                Intent currentGameIntent = new Intent(MainActivity.this, CurrentGameActivity.class);
                MainActivity.this.startActivity(currentGameIntent);
                Toast.makeText(MainActivity.this, "Getting Current Game", Toast.LENGTH_SHORT).show();
            }
        });

        myStats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Show pedometer for the day, kills for the week, deaths for the week
                // Calories burned, distance walked
                Intent myStatsIntent = new Intent(MainActivity.this, MyStatsActivity.class);
                MainActivity.this.startActivity(myStatsIntent);
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

}

