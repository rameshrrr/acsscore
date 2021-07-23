package com.quiqgenie.phonedetailsapp;

public class LocationModel {
    int serialno;
    String location;
    String date;
    String time;
    String latt;
    String longg;
    String address;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }




    public LocationModel() {
    }


    public LocationModel(int serialno, String location, String date, String time,
                         String latt, String longg, String address) {
        this.serialno = serialno;
        this.location = location;
        this.date = date;
        this.time = time;
        this.latt = latt;
        this.longg = longg;
        this.address = address;

    }

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLatt() {
        return latt;
    }

    public void setLatt(String latt) {
        this.latt = latt;
    }

    public String getLongg() {
        return longg;
    }

    public void setLongg(String longg) {
        this.longg = longg;
    }
}
