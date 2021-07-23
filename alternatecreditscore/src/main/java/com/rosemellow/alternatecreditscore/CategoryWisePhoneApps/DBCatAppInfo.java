package com.rosemellow.alternatecreditscore.CategoryWisePhoneApps;

class DBCatAppInfo {
    int serialno;

    String appname;
    String pname;
    String versionName;
    String categoryName;

    public DBCatAppInfo() {
    }

    public DBCatAppInfo(int serialno, String appname, String pname, String versionName,String categoryName) {
        this.serialno = serialno;
        this.appname = appname;
        this.pname = pname;
        this.versionName = versionName;
        this.categoryName=categoryName;   }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
