package com.quiqgenie.phonedetailsapp.Calender;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.opencsv.CSVWriter;
import com.quiqgenie.phonedetailsapp.AppUsage.UsageStatsActivity;
import com.quiqgenie.phonedetailsapp.BaseActivity;
import com.quiqgenie.phonedetailsapp.R;
import com.quiqgenie.phonedetailsapp.SavePref;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalenderActivity extends BaseActivity {

    RecyclerView recyclerview;

    CalenderDB calenderDB;
    Button exportbtn;
    StringBuffer sb = new StringBuffer();
    private Uri CALENDAR_URI;

    public String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle("Calender");


        sb.append("CALENDER" + "\n\n");


        sb.append("Event Name,  Event Date " + "\n\n");


        calenderDB = new CalenderDB(CalenderActivity.this);
        calenderDB.clearDB();

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(CalenderActivity.this));

        readCalendarEvent();


        exportbtn = findViewById(R.id.exportbtn);
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exportDB();

            }
        });


    }

    public ArrayList<CalenderEventsModel> readCalendarEvent() {

        ArrayList<CalenderEventsModel> calenderEventsModels = new ArrayList<>();

        Cursor cursor = CalenderActivity.this.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation"}, null,
                        null, null);
        cursor.moveToFirst();


        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(1);
            String date = getDate(Long.parseLong(cursor.getString(3)));
            String desc = cursor.getString(2);

            Log.e("EFwsdcfdc", phNumber + " " + date + " " + desc);


            calenderEventsModels.add(new CalenderEventsModel(phNumber, date, desc));

            calenderDB.addMoods(new DBCalenderEventsModel(1, phNumber, date, desc));

            sb.append(phNumber + ",  " +
                    date + ",  " +
                    "\n");

            recyclerview.setAdapter(new CalenderEventsAdapter(
                    CalenderActivity.this, calenderEventsModels));


        }


        cursor.close();

        new SavePref(this).setCalenderEvents(sb.toString());



        return calenderEventsModels;
    }


    private void exportDB() {


        CalenderDB dbhelper = new CalenderDB(CalenderActivity.this);
        File exportDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "Calender_" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILDBCalender1233", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {

                String[] arrStr = {curCSV.getString(0) + "",
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3)};
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