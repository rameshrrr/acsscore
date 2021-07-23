package com.rosemellow.alternatecreditscore.SmsModule;

public class SmsListModel {
    String smsData;
    String number;
    String smsDate;
    String time;
    String type;

    public SmsListModel(String smsData, String number, String smsDate, String time, String type) {
        this.smsData = smsData;
        this.number = number;
        this.smsDate = smsDate;
        this.time = time;
        this.type = type;
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
