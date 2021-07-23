package com.quiqgenie.phonedetailsapp.simpleimagegallery;

public class ImageModel {
    int serialno;
    String name;
    String size;
    String uri;

    public ImageModel() {
    }

    public ImageModel(int serialno, String name, String size, String uri) {
        this.serialno = serialno;
        this.name = name;
        this.size = size;
        this.uri = uri;
    }

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
