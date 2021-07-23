package com.rosemellow.alternatecreditscore.PhoneApps;

import android.graphics.drawable.Drawable;

class AppInfo {
    String appname;
    Drawable icon;
    String pname;
    String versionName;

    public AppInfo(String appname, Drawable icon, String pname, String versionName) {
        this.appname = appname;
        this.icon = icon;
        this.pname = pname;
        this.versionName = versionName;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
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
