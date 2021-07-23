package com.rosemellow.alternatecreditscore.PhoneApps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class AllAppsDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PHONEDETAILAllAppsDB";
    private static final String TABLE_PAIN_SCALE = "PHONEDETAILAllApps";


    private static final String KEY_ID = "KEY_ID";

    private static final String KEYAPPNAME = "KEYAPPNAME"; //1
    private static final String KEYPKGNAME = "KEYPKGNAME";//2
    private static final String KEYVERSIONNAME = "KEYVERSIONNAME";//3


    public AllAppsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PAIN_SCALE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEYAPPNAME + " TEXT,"
                + KEYPKGNAME + " TEXT,"
                + KEYVERSIONNAME + " TEXT " + ")";


        db.execSQL(CREATE_CONTACTS_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIN_SCALE);


        onCreate(db);
    }


    public void addMoods(DBAppInfo meterReadingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEYAPPNAME, meterReadingModel.getAppname());
        values.put(KEYPKGNAME, meterReadingModel.getPname());
        values.put(KEYVERSIONNAME, meterReadingModel.getVersionName());


        db.insert(TABLE_PAIN_SCALE, null, values);
        db.close();
    }


    public List<DBAppInfo> getAllMoods() {
        List<DBAppInfo> meterReadingModelList = new ArrayList<DBAppInfo>();

        String selectQuery = "SELECT  * FROM " + TABLE_PAIN_SCALE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DBAppInfo meterReadingModel = new DBAppInfo();
                meterReadingModel.setSerialno(Integer.parseInt(cursor.getString(0)));


                meterReadingModel.setAppname(cursor.getString(1));
                meterReadingModel.setPname(cursor.getString(2));
                meterReadingModel.setVersionName(cursor.getString(3));


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