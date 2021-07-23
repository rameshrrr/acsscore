package com.rosemellow.alternatecreditscore.SmsModule;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Telephony;
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

public class SMSActivity extends BaseActivity {

    SMSDB smsdb;
    Button exportbtn;
    StringBuffer sb = new StringBuffer();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_m_s);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle("SMS List");


        smsdb = new SMSDB(SMSActivity.this);
        smsdb.clearDB();

        sb.append("SMS LOG" + "\n\n");


        sb.append("Number,  Message,  Type,  Time" + "\n\n");


        RecyclerView recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(SMSActivity.this));
        recyclerview.setAdapter(new SMSAdapter(SMSActivity.this, getAllSms()));

        exportbtn = findViewById(R.id.exportbtn);
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exportDB();

            }
        });
    }

    public ArrayList<SmsListModel> getAllSms() {

        ArrayList<SmsListModel> smsListModels = new ArrayList<>();


        ContentResolver cr = SMSActivity.this.getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS = 0;
        if (c != null) {
            totalSMS = c.getCount();

            Log.e("EFwsdcfdc", totalSMS + "");

            if (c.moveToFirst()) {
                for (int j = 0; j < totalSMS; j++) {
                    String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));

                    Date dateFormat = new Date(Long.valueOf(smsDate));
                    String type = "";


                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:
                            type = "inbox";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_SENT:
                            type = "sent";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                            type = "outbox";
                            break;
                        default:
                            break;
                    }


                    String currentDate = new SimpleDateFormat("dd MMM yyyy").format(dateFormat);
                    String currentTime = new SimpleDateFormat("hh:mm:ss").format(dateFormat);


                    smsListModels.add(new SmsListModel(body, number, currentDate, currentTime, type));


                    sb.append(number + ",  " +
                            body + ",  " +
                            type + ",  " +
                            currentDate + " " + currentTime + "" +
                            "\n");


                    smsdb.addMoods(new DBSmsListModel(1, body, number, currentDate, currentTime, type));

                    c.moveToNext();
                }
            }
            new SavePref(this).setSMSData(sb.toString());

            c.close();

        } else {
            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
        }

        return smsListModels;

    }


    private void exportDB() {


        SMSDB dbhelper = new SMSDB(SMSActivity.this);
        File exportDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "SMSMessages_" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILDBSms123", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {

                String[] arrStr = {curCSV.getString(0) + "",
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3),
                        curCSV.getString(4),
                        curCSV.getString(5)};
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