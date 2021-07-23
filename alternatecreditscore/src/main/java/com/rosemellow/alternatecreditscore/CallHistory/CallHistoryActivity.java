package com.rosemellow.alternatecreditscore.CallHistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.opencsv.CSVWriter;
import com.rosemellow.alternatecreditscore.BaseActivity;
import com.rosemellow.alternatecreditscore.R;
import com.rosemellow.alternatecreditscore.SavePref;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CallHistoryActivity extends BaseActivity {

    Button exportbtn;

    CallPhoneDB callPhoneDB;
    StringBuffer sb = new StringBuffer();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle("Call History");


        callPhoneDB = new CallPhoneDB(CallHistoryActivity.this);

        callPhoneDB.clearDB();


        sb.append("CALL LOG" + "\n\n");


        sb.append("Number,  Call Type,  Date,  Call duration,  Location" + "\n\n");





        RecyclerView recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(CallHistoryActivity.this));
        recyclerview.setAdapter(new CallPhoneHistoryAdapter(CallHistoryActivity.this, getCallDetails()));


        exportbtn = findViewById(R.id.exportbtn);
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exportDB();

            }
        });
    }

    private ArrayList<CallPhoneModel> getCallDetails() {

        ArrayList<CallPhoneModel> callPhoneModels = new ArrayList<>();


        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int iso = managedCursor.getColumnIndex(CallLog.Calls.COUNTRY_ISO);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int geocode = managedCursor.getColumnIndex(CallLog.Calls.GEOCODED_LOCATION);


        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String isoStr = managedCursor.getString(iso);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String geocodeStr = managedCursor.getString(geocode);

            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
                case CallLog.Calls.BLOCKED_TYPE:
                    dir = "BLOCKED";
                    break;
            }


            String currentDate = new SimpleDateFormat("dd MMM yyyy").format(callDayTime);
            String currentTime = new SimpleDateFormat("hh:mm:ss").format(callDayTime);


            callPhoneModels.add(new CallPhoneModel(isoStr + " " + phNumber, dir, currentDate, currentTime, callDuration, geocodeStr));


            callPhoneDB.addMoods(new DBCallPhoneModel(1,
                    isoStr + " " + phNumber, dir, currentDate, currentTime, callDuration, geocodeStr));

            sb.append(isoStr + " " + phNumber + ",  " +
                    dir + ",  " +
                    currentDate + " " + currentTime + "" +
                    callDuration + ",  " +
                    geocodeStr + ",  " +
                    "\n");



        }



        managedCursor.close();
        new SavePref(this).setCallLogData(sb.toString());


        return callPhoneModels;
    }


    private void exportDB() {


        CallPhoneDB dbhelper = new CallPhoneDB(CallHistoryActivity.this);
                File exportDir =new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))) ;
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "CallHistory_" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILDBCallHistory1", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {

                String[] arrStr = {curCSV.getString(0) + "",
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3),
                        curCSV.getString(4),
                        curCSV.getString(5),
                        curCSV.getString(6)};
                csvWrite.writeNext(arrStr);


            }
            csvWrite.close();
            curCSV.close();

            Toast.makeText(this, "CSV Stored in Downloads", Toast.LENGTH_SHORT).show();

        } catch (Exception sqlEx) {
            Log.e("DisplayDbMainActivity", sqlEx.getMessage(), sqlEx);
        }
    }


}