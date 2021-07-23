package com.quiqgenie.phonedetailsapp.HardwareSofftware;

import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;
import com.quiqgenie.phonedetailsapp.BaseActivity;
import com.quiqgenie.phonedetailsapp.R;
import com.quiqgenie.phonedetailsapp.SavePref;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HardwareSoftwareInfoActivity extends BaseActivity {

    HardwareSoftwareDB hardwareSoftwareDB;
    Button hardsoftinfoexportbtn;

    TextView devicenametv, internalmemory, devicemanufacturertv, deviceostv, deviceapileveltv, deviceidtv, ramsizetv, availableexternalmemory, availableinternalmemory, externalmemory;
    StringBuffer sb = new StringBuffer();

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware_software_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle("Hardware Software Info");


        sb.append("HARDWARE AND SOFTWARE INFORMATION" + "\n\n");


        sb.append("Device Name  Device Manufacturer  Device OS  Device Api Level  Device ID  RAM Size  Total Internal Memory  Available Internal Memory  Total External Memory  Available External Memory" + "\n\n");


        hardwareSoftwareDB = new HardwareSoftwareDB(HardwareSoftwareInfoActivity.this);


        devicenametv = findViewById(R.id.devicenametv);
        devicemanufacturertv = findViewById(R.id.devicemanufacturertv);
        deviceostv = findViewById(R.id.deviceostv);
        deviceapileveltv = findViewById(R.id.deviceapileveltv);
        deviceidtv = findViewById(R.id.deviceidtv);
        ramsizetv = findViewById(R.id.ramsizetv);


        availableexternalmemory = findViewById(R.id.availableexternalmemory);
        externalmemory = findViewById(R.id.externalmemory);
        availableinternalmemory = findViewById(R.id.availableinternalmemory);
        internalmemory = findViewById(R.id.internalmemory);


        String deviceName = android.os.Build.MODEL;
        String deviceMan = android.os.Build.MANUFACTURER;
        String androidOS = Build.VERSION.RELEASE;
        String myAPI = Build.VERSION.SDK; // API Level
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);  //Device Id
        deviceidtv.setText(deviceID);

        deviceapileveltv.setText(myAPI);


        ActivityManager actManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;  //RAM Size

        String RAMSize = getTotalRAM();
        ramsizetv.setText(RAMSize);


        availableexternalmemory.setText(humanReadableByteCountBin(MemoryStatus.getAvailableExternalMemorySize()) + "");
        availableinternalmemory.setText(humanReadableByteCountBin(MemoryStatus.getAvailableInternalMemorySize()) + "");
        internalmemory.setText(humanReadableByteCountBin(MemoryStatus.getTotalInternalMemorySize()) + "");
        externalmemory.setText(humanReadableByteCountBin(MemoryStatus.getTotalExternalMemorySize()) + "");


        devicemanufacturertv.setText(deviceMan);
        devicenametv.setText(deviceName);
        deviceostv.setText(androidOS);


        sb.append(deviceName + ",  " + deviceMan + ",  " +
                androidOS + ",  " + myAPI + ",  " +
                deviceID + ",  " + RAMSize + ",  " +
                humanReadableByteCountBin(MemoryStatus.getTotalInternalMemorySize()) + ",  " + humanReadableByteCountBin(MemoryStatus.getAvailableInternalMemorySize()) + ",  " +
                humanReadableByteCountBin(MemoryStatus.getTotalExternalMemorySize()) + ",  " + humanReadableByteCountBin(MemoryStatus.getAvailableExternalMemorySize()) + ",  " +
                deviceName + ",  " + deviceMan + " " + "\n");


        hardwareSoftwareDB.clearDB();

        hardsoftinfoexportbtn = findViewById(R.id.hardsoftinfoexportbtn);
        hardsoftinfoexportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                hardwareSoftwareDB.addMoods(new HardSoftModel(1, "Device Name", devicenametv.getText().toString()));
                hardwareSoftwareDB.addMoods(new HardSoftModel(1, "Device Manufacturer : ", devicemanufacturertv.getText().toString()));
                hardwareSoftwareDB.addMoods(new HardSoftModel(1, "Device OS : ", deviceostv.getText().toString()));
                hardwareSoftwareDB.addMoods(new HardSoftModel(1, "Device Api Level : ", deviceapileveltv.getText().toString()));
                hardwareSoftwareDB.addMoods(new HardSoftModel(1, "Device ID : ", deviceidtv.getText().toString()));
                hardwareSoftwareDB.addMoods(new HardSoftModel(1, "RAM Size :", ramsizetv.getText().toString()));
                hardwareSoftwareDB.addMoods(new HardSoftModel(1, "Total Internal Memory", internalmemory.getText().toString()));
                hardwareSoftwareDB.addMoods(new HardSoftModel(1, "Available Internal Memory", availableinternalmemory.getText().toString()));
                hardwareSoftwareDB.addMoods(new HardSoftModel(1, "Total External Memory", externalmemory.getText().toString()));
                hardwareSoftwareDB.addMoods(new HardSoftModel(1, "Available External Memory", availableexternalmemory.getText().toString()));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exportDB();

                    }
                }, 1000);


            }
        });


        new SavePref(this).setHardSoftInfo(sb.toString());


    }

    public String getTotalRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return lastValue;
    }


    private void exportDB() {


        HardwareSoftwareDB dbhelper = new HardwareSoftwareDB(HardwareSoftwareInfoActivity.this);
        File exportDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "HardwareSoftwareInfo_" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILDBHardWareSoftware", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {

                String[] arrStr = {curCSV.getString(0),
                        curCSV.getString(1),
                        curCSV.getString(2)};
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