package com.tony_justin.mobile_app.assassins;

import android.app.Application;

/**
 * Created by Tony Nguyen on 11/2/2017.
 */

public class Globals extends Application {

    private static Globals instance;

    private String userID;

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }

}
