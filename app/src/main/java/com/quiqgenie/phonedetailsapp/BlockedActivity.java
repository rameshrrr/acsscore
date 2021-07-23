package com.quiqgenie.phonedetailsapp;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.BlockedNumberContract;
import android.provider.CallLog;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class BlockedActivity extends BaseActivity {
    TextView call;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked);

        call = findViewById(R.id.call);
        getCallDetails();


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getCallDetails() {

        StringBuffer sb = new StringBuffer();
//        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);

        Cursor managedCursor = getContentResolver().query(BlockedNumberContract.BlockedNumbers.CONTENT_URI,
                new String[]{BlockedNumberContract.BlockedNumbers.COLUMN_ID,
                        BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER,
                        BlockedNumberContract.BlockedNumbers.COLUMN_E164_NUMBER}, null, null, null);

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);/*
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int geocode = managedCursor.getColumnIndex(CallLog.Calls.GEOCODED_LOCATION);*/


        sb.append("Call Details :");
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);/*
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String geocodeStr = managedCursor.getString(geocode);*/

         /*   String dir = null;
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
            }*/
            sb.append("\nPhone Number:--- " + phNumber
            );
            sb.append("\n----------------------------------");
        }
        managedCursor.close();
        call.setText(sb);
    }

    /* + " \nCall Type:--- " + dir +
            " \nCall Date:--- " + callDayTime +
            " \nCall GeoCode:--- " + geocodeStr +
            " \nCall duration in sec :--- " + callDuration*/

}