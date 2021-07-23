package com.quiqgenie.phonedetailsapp.VideoModule;


public class DBVideoData {

    int serialno;

    String name;
    String album;
    String data;
    String duration;
    String date;
    String size;

    public DBVideoData() {
    }

    public DBVideoData(int serialno, String name, String album, String data, String duration, String date, String size) {
        this.serialno = serialno;
        this.name = name;
        this.album = album;
        this.data = data;
        this.duration = duration;
        this.date = date;
        this.size = size;
    }

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
