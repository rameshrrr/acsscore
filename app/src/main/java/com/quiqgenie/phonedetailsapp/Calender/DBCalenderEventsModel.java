package com.quiqgenie.phonedetailsapp.Calender;

public class DBCalenderEventsModel {
    int serialno;
    String eventName;
    String daye;
    String desc;

    public DBCalenderEventsModel(int serialno, String eventName, String daye, String desc) {
        this.serialno = serialno;
        this.eventName = eventName;
        this.daye = daye;
        this.desc = desc;
    }

    public DBCalenderEventsModel() {
    }

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getDaye() {
        return daye;
    }

    public void setDaye(String daye) {
        this.daye = daye;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
