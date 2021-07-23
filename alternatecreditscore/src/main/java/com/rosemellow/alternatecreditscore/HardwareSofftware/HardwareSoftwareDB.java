package com.rosemellow.alternatecreditscore.HardwareSofftware;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class HardwareSoftwareDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PHONEDETAILDB";
    private static final String TABLE_PAIN_SCALE = "PHONEDETAILDBHardWareSoftware";


    private static final String KEY_ID = "KEY_ID";

    private static final String KEYNAME = "SPECIFICATION"; //1
    private static final String KEYANSWER = "RESULT";//2


    public HardwareSoftwareDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PAIN_SCALE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEYNAME + " TEXT,"
                + KEYANSWER + " TEXT " + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIN_SCALE);


        onCreate(db);
    }


    public void addMoods(HardSoftModel meterReadingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEYNAME, meterReadingModel.getSpecification());
        values.put(KEYANSWER, meterReadingModel.getResult());


        db.insert(TABLE_PAIN_SCALE, null, values);
        db.close();
    }


    public List<HardSoftModel> getAllMoods() {
        List<HardSoftModel> meterReadingModelList = new ArrayList<HardSoftModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_PAIN_SCALE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HardSoftModel meterReadingModel = new HardSoftModel();
                meterReadingModel.setSerialno(Integer.parseInt(cursor.getString(0)));


                meterReadingModel.setSpecification(cursor.getString(1));
                meterReadingModel.setResult(cursor.getString(2));


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