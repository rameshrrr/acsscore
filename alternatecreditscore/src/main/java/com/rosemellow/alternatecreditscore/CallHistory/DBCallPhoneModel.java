package com.rosemellow.alternatecreditscore.CallHistory;

public class DBCallPhoneModel {

    int serialno;
    String phoneNumber;
    String callType;
    String callDate;
    String callDayTime;
    String callDuration;
    String geocodeStr;

    public DBCallPhoneModel() {
    }

    public DBCallPhoneModel(int serialno, String phoneNumber, String callType, String callDate, String callDayTime, String callDuration, String geocodeStr) {
        this.serialno = serialno;
        this.phoneNumber = phoneNumber;
        this.callType = callType;
        this.callDate = callDate;
        this.callDayTime = callDayTime;
        this.callDuration = callDuration;
        this.geocodeStr = geocodeStr;
    }

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public String getCallDayTime() {
        return callDayTime;
    }

    public void setCallDayTime(String callDayTime) {
        this.callDayTime = callDayTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getGeocodeStr() {
        return geocodeStr;
    }

    public void setGeocodeStr(String geocodeStr) {
        this.geocodeStr = geocodeStr;
    }
}
