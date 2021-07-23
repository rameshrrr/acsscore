package com.rosemellow.alternatecreditscore.simpleimagegallery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ImageFolderDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PHONEDETAILDBImageFolderDB";
    private static final String TABLE_PAIN_SCALE = "PHONEDETAILDBImageFolder";


    private static final String KEY_ID = "KEY_ID";

    private static final String KEYNAME = "KEYNAME"; //1
    private static final String KEYNUMBER = "KEYNUMBER"; //2


    public ImageFolderDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PAIN_SCALE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEYNAME + " TEXT,"
                + KEYNUMBER + " TEXT " + ")";


        db.execSQL(CREATE_CONTACTS_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIN_SCALE);


        onCreate(db);
    }


    public void addMoods(ImageFolderModel meterReadingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEYNAME, meterReadingModel.getFoldername());
        values.put(KEYNUMBER, meterReadingModel.getNumberofvideos());


        Log.e("EFdxcfdc", "adding" + " " + meterReadingModel.getSerialno());


        db.insert(TABLE_PAIN_SCALE, null, values);
        db.close();
    }


    public List<ImageFolderModel> getAllMoods() {
        List<ImageFolderModel> meterReadingModelList = new ArrayList<ImageFolderModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_PAIN_SCALE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ImageFolderModel meterReadingModel = new ImageFolderModel();
                meterReadingModel.setSerialno(Integer.parseInt(cursor.getString(0)));

                meterReadingModel.setFoldername(cursor.getString(1));
                meterReadingModel.setNumberofvideos(cursor.getString(2));


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