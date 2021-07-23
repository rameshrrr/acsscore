package com.rosemellow.alternatecreditscore.CategoryWisePhoneApps;

import android.graphics.drawable.Drawable;

class CatAppInfo {
    String appname;
    Drawable icon;
    String pname;
    String versionName;

    String categoryName;

    public CatAppInfo(String appname, Drawable icon, String pname, String versionName,String categoryName) {
        this.appname = appname;
        this.icon = icon;
        this.pname = pname;
        this.versionName = versionName;
        this.categoryName=categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
