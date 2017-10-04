package com.tony_justin.mobile_app.assassins;

/**
 * Created by Tony Nguyen on 9/29/2017.
 */

public class Card {
    private String imgURL;
    private String lat;
    private String lon;
    private String time;
    private String caption;

    public Card(String imgURL, String caption, String lat, String lon, String time) {
        this.imgURL = imgURL;
        this.lat = lat;
        this.lon = lon;
        this.time = time;
        this.caption = caption;
    }

    public String getImgURL() {
        return imgURL;
    }
    public String getCaption() { return caption; }
    public String getLat(){ return lat; }
    public String getLon(){ return lon; }
    public String getTime(){ return time; }

    //public void setImgURL(String imgURL) {this.imgURL = imgURL;}
    //public String getTitle() {return title;}
    //public void setTitle(String title) {this.time = time;}
}
