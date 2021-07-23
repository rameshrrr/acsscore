package com.quiqgenie.phonedetailsapp.PhoneApps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.opencsv.CSVWriter;
import com.quiqgenie.phonedetailsapp.BaseActivity;
import com.quiqgenie.phonedetailsapp.R;
import com.quiqgenie.phonedetailsapp.SavePref;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllPhoneActivity extends BaseActivity {

    public ArrayList<AppInfo> res = new ArrayList<>();

    RecyclerView recyclerview;
    AllAppsDB allAppsDB;
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
        this.setTitle("Phone Applications");


        allAppsDB = new AllAppsDB(AllPhoneActivity.this);

        sb.append("USER INSTALLED APPS" + "\n\n");


        sb.append("Category,  App Name" + "\n\n");


        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(AllPhoneActivity.this));

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


            Drawable d = packageInfo.applicationInfo.loadIcon(getPackageManager());


            PackageManager pm = AllPhoneActivity.this.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageInfo.packageName, 0);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int appCategory = applicationInfo.category;
                String categoryTitle = (String) ApplicationInfo.getCategoryTitle(AllPhoneActivity.this, appCategory);

                Log.e("FEfdasxdsc", categoryTitle + " " + packageInfo.packageName);

                if (categoryTitle == null) {

                    sb.append("Others" + ",   " +
                            packageInfo.applicationInfo.loadLabel(getPackageManager()).toString() + "  " +
                            "\n");

                } else {
                    Log.e("FEfdasxdscNEW", categoryTitle + " " + packageInfo.packageName);

                    res.add(new AppInfo(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString(),
                            d, packageInfo.packageName, packageInfo.versionName
                    ));

                    allAppsDB.addMoods(new DBAppInfo(1, packageInfo.applicationInfo.loadLabel(getPackageManager()).toString(),
                            packageInfo.packageName, packageInfo.versionName
                    ));


                    sb.append(categoryTitle + ",   " +
                            packageInfo.applicationInfo.loadLabel(getPackageManager()).toString() + "  " +
                            "\n");

                }

            }


        }

        new SavePref(this).setInstalledApps(sb.toString());

        recyclerview.setAdapter(new DetectedAppAdapter(AllPhoneActivity.this, res));


        return res.size();
    }

    private void exportDB() {


        AllAppsDB dbhelper = new AllAppsDB(AllPhoneActivity.this);
        File exportDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "AllApps_" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILAllApps", null);
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

    public class DetectedAppAdapter extends RecyclerView.Adapter<DetectedAppAdapter.ViewHolder> {
        Context context;
        View view1;
        ViewHolder viewHolder1;
        ArrayList<AppInfo> arrayList;

        Handler handler = new Handler();
        Runnable runnable;

        public DetectedAppAdapter(Context context, ArrayList<AppInfo> arrayList) {
            try {
                this.arrayList = arrayList;
                this.context = context;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            try {

                view1 = LayoutInflater.from(context).inflate(R.layout.single_item_app_info, parent, false);
                viewHolder1 = new ViewHolder(view1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return viewHolder1;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            try {
                holder.apppackagenametv.setText(arrayList.get(position).getPname());
                holder.appnametv.setText(arrayList.get(position).getAppname());
                holder.appversionnametv.setText(arrayList.get(position).getVersionName());


                Glide.with(context)
                        .load(arrayList.get(position).getIcon())
                        .into(holder.appiconiv);

            } catch (Exception e) {

            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView appversionnametv, apppackagenametv, appnametv;
            ImageView appiconiv;

            public ViewHolder(View v) {
                super(v);

                appversionnametv = v.findViewById(R.id.appversionnametv);
                apppackagenametv = v.findViewById(R.id.apppackagenametv);
                appnametv = v.findViewById(R.id.appnametv);
                appiconiv = v.findViewById(R.id.appiconiv);

            }
        }
    }


}