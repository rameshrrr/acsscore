package com.rosemellow.alternatecreditscore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class LocationDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PHONEDETAILLocationnDB1";
    private static final String TABLE_PAIN_SCALE = "PHONEDETAILLocationn1";


    private static final String KEY_ID = "KEY_ID";

    private static final String KEYNAME = "KEYNAME"; //1
    private static final String KEYDATE = "KEYDATE";//2
    private static final String KEYTIME = "KEYTIME";//2
    private static final String KEYLAT = "KEYLAT";//2
    private static final String KEYLONG = "KEYLONG";//2
    private static final String KEYADDRESS = "KEYADDRESS";//2



    public LocationDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PAIN_SCALE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEYNAME + " TEXT,"
                + KEYDATE + " TEXT,"
                + KEYTIME + " KEYTIME,"
                + KEYLAT + " TEXT,"
                + KEYLONG + " TEXT,"
                + KEYADDRESS + " TEXT " + ")";


        db.execSQL(CREATE_CONTACTS_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIN_SCALE);


        onCreate(db);
    }


    public void addMoods(LocationModel meterReadingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEYNAME, meterReadingModel.getLatt() + "," + meterReadingModel.getLongg());
        values.put(KEYDATE, meterReadingModel.getDate());
        values.put(KEYTIME, meterReadingModel.getTime());
        values.put(KEYLAT, meterReadingModel.getLatt());
        values.put(KEYLONG, meterReadingModel.getLongg());
        values.put(KEYADDRESS, meterReadingModel.getAddress());


        db.insert(TABLE_PAIN_SCALE, null, values);
        db.close();
    }


    public List<LocationModel> getAllMoods() {
        List<LocationModel> meterReadingModelList = new ArrayList<LocationModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_PAIN_SCALE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LocationModel meterReadingModel = new LocationModel();
                meterReadingModel.setSerialno(Integer.parseInt(cursor.getString(0)));


                meterReadingModel.setLocation(cursor.getString(1));
                meterReadingModel.setDate(cursor.getString(2));
                meterReadingModel.setTime(cursor.getString(3));
                meterReadingModel.setLatt(cursor.getString(4));
                meterReadingModel.setLongg(cursor.getString(5));
                meterReadingModel.setAddress(cursor.getString(6));



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