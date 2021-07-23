package com.quiqgenie.phonedetailsapp.VideoModule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class VideoDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PHONEDETAILDBVideoDB";
    private static final String TABLE_PAIN_SCALE = "PHONEDETAILDBVideo";


    private static final String KEY_ID = "KEY_ID";

    private static final String KEYNAME = "KEYNUMBER"; //1
    private static final String KEYALBUM = "KEYCALLTYPE"; //2
    private static final String KEYDATA = "KEYCALLDATE"; //3
    private static final String KEYDURATION = "KEYTIME"; //4
    private static final String KEYDATE = "KEYDURATION"; //5
    private static final String KEYSIZE = "KEYGEOCODE"; //6


    public VideoDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PAIN_SCALE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEYNAME + " TEXT,"
                + KEYALBUM + " TEXT,"
                + KEYDATA + " TEXT,"
                + KEYDURATION + " TEXT,"
                + KEYDATE + " TEXT,"
                + KEYSIZE + " TEXT " + ")";


        db.execSQL(CREATE_CONTACTS_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIN_SCALE);


        onCreate(db);
    }


    public void addMoods(DBVideoData meterReadingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEYNAME, meterReadingModel.getName());
        values.put(KEYALBUM, meterReadingModel.getAlbum());
        values.put(KEYDATA, meterReadingModel.getData());
        values.put(KEYDURATION, meterReadingModel.getDuration());
        values.put(KEYDATE, meterReadingModel.getDate());
        values.put(KEYSIZE, meterReadingModel.getSize());


        Log.e("EFdxcfdc", "adding" + " " + meterReadingModel.getSerialno());


        db.insert(TABLE_PAIN_SCALE, null, values);
        db.close();
    }


    public List<DBVideoData> getAllMoods() {
        List<DBVideoData> meterReadingModelList = new ArrayList<DBVideoData>();

        String selectQuery = "SELECT  * FROM " + TABLE_PAIN_SCALE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DBVideoData meterReadingModel = new DBVideoData();
                meterReadingModel.setSerialno(Integer.parseInt(cursor.getString(0)));

                meterReadingModel.setName(cursor.getString(1));
                meterReadingModel.setAlbum(cursor.getString(2));
                meterReadingModel.setData(cursor.getString(3));
                meterReadingModel.setDuration(cursor.getString(4));
                meterReadingModel.setDate(cursor.getString(5));
                meterReadingModel.setSize(cursor.getString(6));


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