package com.rosemellow.alternatecreditscore;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class SavePref {
    public final static String PREFS_NAME = "PSDreamsPrefs";
    private static Context context;

    public SavePref(Context context) {
        this.context = context;
    }






    public static String getHardSoftInfo() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("HardSoftInfo", "");
    }

    public static void setHardSoftInfo(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("HardSoftInfo", value);
        editor.apply();
    }



    public static String getMyPhoneNumber() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("MyPhoneNumber", "");
    }

    public static void setMyPhoneNumber(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("MyPhoneNumber", value);
        editor.apply();
    }
    public static String getMyIMEI() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("MyIMEI", "");
    }
    public static void setMyIMEI(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("MyIMEI", value);
        editor.apply();
    }




    public static String getSMSData() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("SMSData", "");
    }

    public static void setSMSData(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("SMSData", value);
        editor.apply();
    }



    public static String getContactsList() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("ContactsList", "");
    }

    public static void setContactsList(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ContactsList", value);
        editor.apply();
    }


    public static String getCallLogData() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("CallLogData", "");
    }

    public static void setCallLogData(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CallLogData", value);
        editor.apply();
    }



    public static String getInstalledApps() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("InstalledApps", "");
    }

    public static void setInstalledApps(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("InstalledApps", value);
        editor.apply();
    }






    public static String getLocation() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("Location", "");
    }

    public static void setLocation(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Location", value);
        editor.apply();
    }







    public static String getAppUsage() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("AppUsage", "");
    }

    public static void setAppUsage(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("AppUsage", value);
        editor.apply();
    }









    public static String getCalenderEvents() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("CalenderEvents", "");
    }

    public static void setCalenderEvents(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CalenderEvents", value);
        editor.apply();
    }






    public static String getCatWiseApps() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("CatWiseApps", "");
    }

    public static void setCatWiseApps(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CatWiseApps", value);
        editor.apply();
    }


    public static String getImagesFolders() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("ImagesFolders", "");
    }

    public static void setImagesFolders(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ImagesFolders", value);
        editor.apply();
    }





    public static String getVideosFolders() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString("VideosFolders", "");
    }

    public static void setVideosFolders(String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("VideosFolders", value);
        editor.apply();
    }

}


