package com.quiqgenie.phonedetailsapp.SmsModule;

public class DBSmsListModel {
    int serialno;
    String smsData;
    String number;
    String smsDate;
    String time;
    String type;

    public DBSmsListModel() {


    }

    public DBSmsListModel(int serialno, String smsData, String number, String smsDate, String time, String type) {
        this.serialno = serialno;
        this.smsData = smsData;
        this.number = number;
        this.smsDate = smsDate;
        this.time = time;
        this.type = type;
    }

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSmsData() {
        return smsData;
    }

    public void setSmsData(String smsData) {
        this.smsData = smsData;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSmsDate() {
        return smsDate;
    }

    public void setSmsDate(String smsDate) {
        this.smsDate = smsDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
