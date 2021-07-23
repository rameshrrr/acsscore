package com.quiqgenie.phonedetailsapp.CallHistory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class CallPhoneDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PHONEDETAILDB1";
    private static final String TABLE_PAIN_SCALE = "PHONEDETAILDBCallHistory1";


    private static final String KEY_ID = "KEY_ID";

    private static final String KEYNUMBER = "KEYNUMBER"; //1
    private static final String KEYCALLTYPE = "KEYCALLTYPE"; //2
    private static final String KEYCALLDATE = "KEYCALLDATE"; //3
    private static final String KEYTIME = "KEYTIME"; //4
    private static final String KEYDURATION = "KEYDURATION"; //5
    private static final String KEYGEOCODE = "KEYGEOCODE"; //6


    public CallPhoneDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PAIN_SCALE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEYNUMBER + " TEXT,"
                + KEYCALLTYPE + " TEXT,"
                + KEYCALLDATE + " TEXT,"
                + KEYTIME + " TEXT,"
                + KEYDURATION + " TEXT,"
                + KEYGEOCODE + " TEXT " + ")";


        db.execSQL(CREATE_CONTACTS_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIN_SCALE);


        onCreate(db);
    }


    public void addMoods(DBCallPhoneModel meterReadingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEYNUMBER, meterReadingModel.getPhoneNumber());
        values.put(KEYCALLTYPE, meterReadingModel.getCallType());
        values.put(KEYCALLDATE, meterReadingModel.getCallDate());
        values.put(KEYTIME, meterReadingModel.getCallDayTime());
        values.put(KEYDURATION, meterReadingModel.getCallDuration());
        values.put(KEYGEOCODE, meterReadingModel.getGeocodeStr());

        Log.e("EFdxcfdc", "adding" + " " + meterReadingModel.getSerialno());


        db.insert(TABLE_PAIN_SCALE, null, values);
        db.close();
    }


    public List<DBCallPhoneModel> getAllMoods() {
        List<DBCallPhoneModel> meterReadingModelList = new ArrayList<DBCallPhoneModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_PAIN_SCALE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DBCallPhoneModel meterReadingModel = new DBCallPhoneModel();
                meterReadingModel.setSerialno(Integer.parseInt(cursor.getString(0)));

                meterReadingModel.setPhoneNumber(cursor.getString(1));
                meterReadingModel.setCallType(cursor.getString(2));
                meterReadingModel.setCallDate(cursor.getString(3));
                meterReadingModel.setCallDayTime(cursor.getString(4));
                meterReadingModel.setCallDuration(cursor.getString(5));
                meterReadingModel.setGeocodeStr(cursor.getString(6));


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