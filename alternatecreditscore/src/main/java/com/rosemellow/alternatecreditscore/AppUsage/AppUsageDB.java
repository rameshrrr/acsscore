package com.rosemellow.alternatecreditscore.AppUsage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class AppUsageDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PHONEDETAILAllAppsUSaageDB";
    private static final String TABLE_PAIN_SCALE = "PHONEDETAILAllAppsUSaage";


    private static final String KEY_ID = "KEY_ID";

    private static final String KEYAPPNAME = "KEYAPPNAME"; //1
    private static final String KEYLASTUSED = "KEYLASTUSED";//3
    private static final String KEYTOTALTIME = "KEYTOTALTIME";//3


    public AppUsageDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PAIN_SCALE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEYAPPNAME + " TEXT,"
                + KEYLASTUSED + " TEXT,"
                + KEYTOTALTIME + " TEXT " + ")";


        db.execSQL(CREATE_CONTACTS_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIN_SCALE);


        onCreate(db);
    }


    public void addMoods(AppUsageModel meterReadingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEYAPPNAME, meterReadingModel.getAppname());
        values.put(KEYLASTUSED, meterReadingModel.getLastused());
        values.put(KEYTOTALTIME, meterReadingModel.getUsagetime());

        db.insert(TABLE_PAIN_SCALE, null, values);
        db.close();
    }


    public List<AppUsageModel> getAllMoods() {
        List<AppUsageModel> meterReadingModelList = new ArrayList<AppUsageModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_PAIN_SCALE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AppUsageModel meterReadingModel = new AppUsageModel();
                meterReadingModel.setSerialno(Integer.parseInt(cursor.getString(0)));


                meterReadingModel.setAppname(cursor.getString(1));
                meterReadingModel.setLastused(cursor.getString(2));
                meterReadingModel.setUsagetime(cursor.getString(3));



                meterReadingModelList.add(meterReadingModel);
            } while (cursor.moveToNext());
        }


        return meterReadingModelList;
    }


    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();

        String clearDBQuery = "DELETE FROM " + TABLE_PAIN_SCALE;
        db.execSQL(clearDBQuery);
    }


}