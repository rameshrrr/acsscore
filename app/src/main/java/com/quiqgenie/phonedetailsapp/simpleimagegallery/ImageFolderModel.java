package com.quiqgenie.phonedetailsapp.simpleimagegallery;

public class ImageFolderModel {

    int serialno;

    String foldername;
    String numberofvideos;

    public ImageFolderModel() {
    }

    public ImageFolderModel(int serialno, String foldername, String numberofvideos) {
        this.serialno = serialno;
        this.foldername = foldername;
        this.numberofvideos = numberofvideos;
    }

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public String getNumberofvideos() {
        return numberofvideos;
    }

    public void setNumberofvideos(String numberofvideos) {
        this.numberofvideos = numberofvideos;
    }
}
