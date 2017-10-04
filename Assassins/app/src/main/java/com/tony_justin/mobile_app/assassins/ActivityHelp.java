package com.tony_justin.mobile_app.assassins;

/**
 * Created by Tony Nguyen on 9/29/2017.
 */

import android.content.ComponentName;
import android.content.Intent;
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
import android.widget.ExpandableListView;

import com.tony_justin.mobile_app.assassin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityHelp extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ExpandableListView listView;
    private ExpendableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        listView = (ExpandableListView) findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpendableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view3);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        Button connectLogout = (Button)findViewById(R.id.connectLogout);
        connectLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginScreen = new Intent(ActivityHelp.this, LoginActivity.class);
                ComponentName cn = loginScreen.getComponent();

                Intent logOut = IntentCompat.makeRestartActivityTask(cn);

                startActivity(logOut);
            }
        });
    }
    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        listDataHeader.add("Getting Started");
        listDataHeader.add("FAQ");
        listDataHeader.add("Contact Support");
        listDataHeader.add("Suggest Something");
        List<String> gettingStarted = new ArrayList<>();
        gettingStarted.add("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod \n " +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " );
        List<String> faq = new ArrayList<>();
        faq.add("1. Lorem ipsum dolor sit amet, \n\n consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        faq.add("2. Lorem ipsum dolor sit amet, \n\n consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        faq.add("3. Lorem ipsum dolor sit amet, \n\n consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        faq.add("4. Lorem ipsum dolor sit amet, \n\n consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        faq.add("5. Lorem ipsum dolor sit amet, \n\n consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        List<String> contactSupport = new ArrayList<>();
        contactSupport.add("Any questions please contact Tony & Justin");
        List<String> suggestSomething= new ArrayList<>();
        suggestSomething.add("Any suggestions email -> ...");
        listHash.put(listDataHeader.get(0),gettingStarted);
        listHash.put(listDataHeader.get(1),faq);
        listHash.put(listDataHeader.get(2),contactSupport);
        listHash.put(listDataHeader.get(3),suggestSomething);
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
                Intent homeIntent = new Intent(ActivityHelp.this, MainActivity.class);
                ActivityHelp.this.startActivity(homeIntent);
                break;
            }
            case R.id.ic_search: {
                Intent searchIntent = new Intent(ActivityHelp.this, ActivitySearch.class);
                ActivityHelp.this.startActivity(searchIntent);
                break;
            }
            case R.id.ic_connect: {
                Intent connectIntent = new Intent(ActivityHelp.this, ActivityConnect.class);
                ActivityHelp.this.startActivity(connectIntent);
                break;
            }
            case R.id.ic_settings: {
                Intent settingsIntent = new Intent(ActivityHelp.this, ActivitySettings.class);
                ActivityHelp.this.startActivity(settingsIntent);
                break;
            }
            case R.id.ic_help: {
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
