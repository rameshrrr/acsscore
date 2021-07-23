package com.quiqgenie.phonedetailsapp.CategoryWisePhoneApps;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.opencsv.CSVWriter;
import com.quiqgenie.phonedetailsapp.BaseActivity;
import com.quiqgenie.phonedetailsapp.R;
import com.quiqgenie.phonedetailsapp.SavePref;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CatAllPhoneActivity extends BaseActivity {

    public ArrayList<CatAppInfo> res = new ArrayList<>();

    RecyclerView recyclerview;
    CatAllAppsDB catAllAppsDB;
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
        setContentView(R.layout.activity_all_phone);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle("Category Wise Phone Applications");


        sb.append("CATEGORY WISE PHONE APPS" + "\n\n");


        sb.append("App Name,  App Category,  App Version,  App Package Name " + "\n\n");


        catAllAppsDB = new CatAllAppsDB(CatAllPhoneActivity.this);


        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(CatAllPhoneActivity.this));

        try {
            getPackageList();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        exportbtn = findViewById(R.id.exportbtn);
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exportDB();

            }
        });

    }


    public int getPackageList() throws PackageManager.NameNotFoundException {

        res = new ArrayList<>();
        List installedPackages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = (PackageInfo) installedPackages.get(i);


            PackageManager pm = CatAllPhoneActivity.this.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageInfo.packageName, 0);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int appCategory = applicationInfo.category;
                String categoryTitle = (String) ApplicationInfo.getCategoryTitle(CatAllPhoneActivity.this, appCategory);


                if (categoryTitle == null) {
                    Drawable d = packageInfo.applicationInfo.loadIcon(getPackageManager());

                    res.add(new CatAppInfo(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString(),
                            d, packageInfo.packageName, packageInfo.versionName, "Others"
                    ));


                    sb.append(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString() + ",  " +
                            "Others" + ",  " +
                            packageInfo.versionName + ",  " +
                            packageInfo.packageName + ",  " +
                            "\n");

                } else {
                    Drawable d = packageInfo.applicationInfo.loadIcon(getPackageManager());

                    Log.e("FEfdasxdscNEW", categoryTitle + " " + packageInfo.packageName);

                    res.add(new CatAppInfo(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString(),
                            d, packageInfo.packageName, packageInfo.versionName, categoryTitle
                    ));


                    sb.append(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString() + ",  " +
                            "categoryTitle" + ",  " +
                            packageInfo.versionName + ",  " +
                            packageInfo.packageName + ",  " +
                            "\n");

                }

            }


        }


        groupDataIntoHashMap(res);
        recyclerview.setAdapter(new NewTransactionAdapter(CatAllPhoneActivity.this
                , groupDataIntoHashMap(res)));

        new SavePref(this).setCatWiseApps(sb.toString());

        return res.size();
    }

    private void exportDB() {


        CatAllAppsDB dbhelper = new CatAllAppsDB(CatAllPhoneActivity.this);
                File exportDir =new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))) ;
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "CatWiseAllApps_" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILAllCatApps", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {

                String[] arrStr = {curCSV.getString(0) + "",
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3),
                        curCSV.getString(4)};
                csvWrite.writeNext(arrStr);


            }
            csvWrite.close();
            curCSV.close();

            Toast.makeText(this, "CSV Stored in Downloads", Toast.LENGTH_SHORT).show();

        } catch (Exception sqlEx) {
            Log.e("DisplayDbMainActivity", sqlEx.getMessage(), sqlEx);
        }
    }


    private HashMap<String, ArrayList<CatAppInfo>> groupDataIntoHashMap(ArrayList<CatAppInfo> listOfPojosOfJsonArray) {

        HashMap<String, ArrayList<CatAppInfo>> groupedHashMap = new HashMap<>();

        for (CatAppInfo pojoOfJsonArray : listOfPojosOfJsonArray) {

            String hashMapKey = pojoOfJsonArray.getCategoryName();

            if (groupedHashMap.containsKey(hashMapKey)) {

                groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                ArrayList<CatAppInfo> list = new ArrayList<>();
                list.add(pojoOfJsonArray);
                groupedHashMap.put(hashMapKey, list);
            }
        }


        return groupedHashMap;
    }


}