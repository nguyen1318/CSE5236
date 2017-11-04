package com.tony_justin.mobile_app.assassins;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Tony Nguyen on 11/2/2017.
 */

public class PlayerInfo extends Application {

    private static PlayerInfo instance;

    private String userID;

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }


    private String email;

    public String getEmail(){
        return userID;
    }

    public void setEmail(String email){
        this.email = email;
    }


    private LatLng latLng;

    public LatLng getLatLng(){ return latLng;}

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }


    private boolean legit;

    public boolean getLegit() { return legit;}

    public void setLegit(boolean legit) {
        this.legit = legit;
    }


    private boolean alive;

    public boolean getAlive() { return alive; }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public static synchronized PlayerInfo getInstance(){
        if(instance==null){
            instance=new PlayerInfo();
        }
        return instance;
    }

}
