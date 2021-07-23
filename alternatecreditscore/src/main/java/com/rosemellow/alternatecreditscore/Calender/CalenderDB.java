package com.rosemellow.alternatecreditscore.Calender;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class CalenderDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PHONEDETAILDB1233";
    private static final String TABLE_PAIN_SCALE = "PHONEDETAILDBCalender1233";


    private static final String KEY_ID = "KEY_ID";

    private static final String KEYNAME = "KEYNAME"; //1
    private static final String KEYDATE = "KEYDATE";//2
    private static final String KEYDESC = "KEYDESC";//3


    public CalenderDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PAIN_SCALE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEYNAME + " TEXT,"
                + KEYDATE + " TEXT,"
                + KEYDESC + " TEXT " + ")";


        db.execSQL(CREATE_CONTACTS_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIN_SCALE);


        onCreate(db);
    }


    public void addMoods(DBCalenderEventsModel meterReadingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEYNAME, meterReadingModel.getEventName());
        values.put(KEYDATE, meterReadingModel.getDaye());
        values.put(KEYDESC, meterReadingModel.getDesc());


        db.insert(TABLE_PAIN_SCALE, null, values);
        db.close();
    }


    public List<DBCalenderEventsModel> getAllMoods() {
        List<DBCalenderEventsModel> meterReadingModelList = new ArrayList<DBCalenderEventsModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_PAIN_SCALE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DBCalenderEventsModel meterReadingModel = new DBCalenderEventsModel();
                meterReadingModel.setSerialno(Integer.parseInt(cursor.getString(0)));


                meterReadingModel.setEventName(cursor.getString(1));
                meterReadingModel.setDaye(cursor.getString(2));
                meterReadingModel.setDesc(cursor.getString(3));


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