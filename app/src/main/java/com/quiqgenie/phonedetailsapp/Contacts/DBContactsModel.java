package com.quiqgenie.phonedetailsapp.Contacts;

public class DBContactsModel {

    int serialno;
    String name;
    String number;

    public DBContactsModel(int serialno, String name, String number) {
        this.serialno = serialno;
        this.name = name;
        this.number = number;
    }

    public DBContactsModel() {
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
