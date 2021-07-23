package com.quiqgenie.phonedetailsapp.Calender;

public class CalenderEventsModel {
    String eventName;
    String daye;
    String desc;

    public CalenderEventsModel(String eventName, String daye, String desc) {
        this.eventName = eventName;
        this.daye = daye;
        this.desc = desc;
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
