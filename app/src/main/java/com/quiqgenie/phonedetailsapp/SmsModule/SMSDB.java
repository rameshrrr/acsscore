package com.quiqgenie.phonedetailsapp.SmsModule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class SMSDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PHONEDETAILDB123";
    private static final String TABLE_PAIN_SCALE = "PHONEDETAILDBSms123";


    private static final String KEY_ID = "KEY_ID";

    private static final String KEYSMSDATA = "KEYSMSDATA"; //1
    private static final String KEYNUMBER = "KEYNUMBER"; //2
    private static final String KEYCALLDATE = "KEYCALLDATE"; //3
    private static final String KEYTIME = "KEYTIME"; //4
    private static final String KEYTYPE = "KEYTYPE"; //5


    public SMSDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PAIN_SCALE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEYSMSDATA + " TEXT,"
                + KEYNUMBER + " TEXT,"
                + KEYCALLDATE + " TEXT,"
                + KEYTIME + " TEXT,"
                + KEYTYPE + " TEXT " + ")";


        db.execSQL(CREATE_CONTACTS_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIN_SCALE);


        onCreate(db);
    }


    public void addMoods(DBSmsListModel meterReadingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(KEYSMSDATA, meterReadingModel.getSmsData());
        values.put(KEYNUMBER, meterReadingModel.getNumber());
        values.put(KEYCALLDATE, meterReadingModel.getSmsDate());
        values.put(KEYTIME, meterReadingModel.getTime());
        values.put(KEYTYPE, meterReadingModel.getType());


        db.insert(TABLE_PAIN_SCALE, null, values);
        db.close();
    }


    public List<DBSmsListModel> getAllMoods() {
        List<DBSmsListModel> meterReadingModelList = new ArrayList<DBSmsListModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_PAIN_SCALE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DBSmsListModel meterReadingModel = new DBSmsListModel();
                meterReadingModel.setSerialno(Integer.parseInt(cursor.getString(0)));

                meterReadingModel.setSmsData(cursor.getString(1));
                meterReadingModel.setNumber(cursor.getString(2));
                meterReadingModel.setSmsDate(cursor.getString(3));
                meterReadingModel.setTime(cursor.getString(4));
                meterReadingModel.setType(cursor.getString(5));


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