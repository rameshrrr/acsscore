package com.quiqgenie.phonedetailsapp.PhoneApps;

class DBAppInfo {
    int serialno;

    String appname;
    String pname;
    String versionName;

    public DBAppInfo() {
    }

    public DBAppInfo(int serialno, String appname, String pname, String versionName) {
        this.serialno = serialno;
        this.appname = appname;
        this.pname = pname;
        this.versionName = versionName;
    }

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }


    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
