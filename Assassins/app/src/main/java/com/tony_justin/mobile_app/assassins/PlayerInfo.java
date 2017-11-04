package com.tony_justin.mobile_app.assassins;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Tony Nguyen on 11/2/2017.
 */

public class PlayerInfo extends Application {

    private static PlayerInfo instance;
    public PlayerInfo (String email, LatLng latLng, boolean legit){

    }

    private static String userID;
    private static String email;
    private static LatLng latLng;
    private static boolean legit;
    private static boolean alive;

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }


    public String getEmail(){
        return userID;
    }

    public void setEmail(String email){
        this.email = email;
    }


    public LatLng getLatLng(){ return latLng;}

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public boolean getLegit() { return legit;}

    public void setLegit(boolean legit) {
        this.legit = legit;
    }

    public boolean getAlive() { return alive; }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public static synchronized PlayerInfo getInstance(){
        if(instance==null){
            instance=new PlayerInfo(email, latLng, legit);
        }
        return instance;
    }

}
