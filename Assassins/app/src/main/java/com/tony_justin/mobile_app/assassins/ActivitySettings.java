package com.tony_justin.mobile_app.assassins;

/**
 * Created by Tony Nguyen on 9/29/2017.
 */

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tony_justin.mobile_app.assassin.R;

/**
 * Created by User on 4/15/2017.
 */

public class ActivitySettings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UserPreferenceFragment.OnFragmentInteractionListener {


    private DrawerLayout drawer;
    private NavigationView navigationView;

    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar4);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view4);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        UserPreferenceFragment mPrefsFragment = new UserPreferenceFragment();
        mFragmentTransaction.replace(R.id.fragment_user_preference, mPrefsFragment);
        mFragmentTransaction.commit();

        Button connectLogout = (Button)findViewById(R.id.connectLogout);
        connectLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginScreen = new Intent(ActivitySettings.this, LoginActivity.class);
                ComponentName cn = loginScreen.getComponent();

                Intent logOut = IntentCompat.makeRestartActivityTask(cn);

                startActivity(logOut);
            }
        });
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
                Intent homeIntent = new Intent(ActivitySettings.this, MainActivity.class);
                ActivitySettings.this.startActivity(homeIntent);
                break;
            }
            case R.id.ic_search: {
                Intent searchIntent = new Intent(ActivitySettings.this, ActivitySearch.class);
                ActivitySettings.this.startActivity(searchIntent);
                break;
            }
            case R.id.ic_connect: {
                Intent connectIntent = new Intent(ActivitySettings.this, ActivityConnect.class);
                ActivitySettings.this.startActivity(connectIntent);
                break;
            }
            case R.id.ic_settings: {
                break;
            }
            case R.id.ic_help: {
                Intent helpIntent = new Intent(ActivitySettings.this, ActivityHelp.class);
                ActivitySettings.this.startActivity(helpIntent);
                break;
            }
        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View mDrawerListView = inflater.inflate(R.layout.drawer_list_item, container, false);
        mDrawerListView.setFitsSystemWindows(true);
        return mDrawerListView;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

