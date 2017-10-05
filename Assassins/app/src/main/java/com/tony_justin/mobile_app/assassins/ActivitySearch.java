package com.tony_justin.mobile_app.assassins;

/**
 * Created by Tony Nguyen on 9/29/2017.
 */

import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.tony_justin.mobile_app.assassin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/15/2017.
 */

public class ActivitySearch extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayAdapter<String> searchAdapter;
    List<String> searchArray;
    ListView mSearchList;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        mSearchList = (ListView) findViewById(R.id.searchList);
        searchArray = new ArrayList<String>();

        searchAdapter = new ArrayAdapter<String>(this, R.layout.list_item, searchArray);
        mSearchList.setAdapter(searchAdapter);

        Button connectLogout = (Button)findViewById(R.id.connectLogout);
        connectLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginScreen = new Intent(ActivitySearch.this, LoginActivity.class);
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
                Intent homeIntent = new Intent(ActivitySearch.this, MainActivity.class);
                ActivitySearch.this.startActivity(homeIntent);
                break;
            }
            case R.id.ic_search: {
                break;
            }
            case R.id.ic_connect: {
                Intent connectIntent = new Intent(ActivitySearch.this, ActivityConnect.class);
                ActivitySearch.this.startActivity(connectIntent);
                break;
            }
            case R.id.ic_settings: {
                Intent settingsIntent = new Intent(ActivitySearch.this, ActivitySettings.class);
                ActivitySearch.this.startActivity(settingsIntent);
                break;
            }
            case R.id.ic_help: {
                Intent helpIntent = new Intent(ActivitySearch.this, ActivityHelp.class);
                ActivitySearch.this.startActivity(helpIntent);
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
}

