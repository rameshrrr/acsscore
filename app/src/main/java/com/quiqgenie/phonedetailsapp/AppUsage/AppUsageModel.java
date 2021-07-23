package com.quiqgenie.phonedetailsapp.AppUsage;

public class AppUsageModel {
    int serialno;
    String appname;
    String lastused;
    String usagetime;

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public AppUsageModel() {
    }

    public AppUsageModel(int serialno, String appname, String lastused, String usagetime) {
        this.serialno = serialno;
        this.appname = appname;
        this.lastused = lastused;
        this.usagetime = usagetime;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getLastused() {
        return lastused;
    }

    public void setLastused(String lastused) {
        this.lastused = lastused;
    }

    public String getUsagetime() {
        return usagetime;
    }

    public void setUsagetime(String usagetime) {
        this.usagetime = usagetime;
    }
}
