package com.rosemellow.alternatecreditscore;

import android.app.AppOpsManager;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.rosemellow.alternatecreditscore.AppUsage.UsageStatsActivity;
import com.rosemellow.alternatecreditscore.Calender.CalenderActivity;
import com.rosemellow.alternatecreditscore.CallHistory.CallHistoryActivity;
import com.rosemellow.alternatecreditscore.CategoryWisePhoneApps.CatAllPhoneActivity;
import com.rosemellow.alternatecreditscore.Contacts.ContactsActivity;
import com.rosemellow.alternatecreditscore.HardwareSofftware.HardwareSoftwareInfoActivity;
import com.rosemellow.alternatecreditscore.PhoneApps.AllPhoneActivity;
import com.rosemellow.alternatecreditscore.SmsModule.SMSActivity;
import com.rosemellow.alternatecreditscore.VideoModule.VideoFolderActivity;
import com.rosemellow.alternatecreditscore.simpleimagegallery.ImagesMainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class MainActivity extends BaseActivity {

    Button hardsoftinfobtn;
    Button locationbtn;
    Button showtextstbtn;
    Button callhistorybtn;
    Button smslistbtn;
    Button calendereventsbtn;
    Button allphoneappsbtn;
    Button allcatwisephoneappsbtn;

    Button allphoneappsusagebtn;
    Button contactslistbtn;

    public static PermissionStatus getUsageStatsPermissionsStatus(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return PermissionStatus.CANNOT_BE_GRANTED;
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        final int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_DEFAULT ?
                (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED)
                : (mode == AppOpsManager.MODE_ALLOWED);
        return granted ? PermissionStatus.GRANTED : PermissionStatus.DENIED;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUsageStatsPermissionsStatus(MainActivity.this);

//        generateNoteOnSD(MainActivity.this,"abcd.txt","uxcnuwefdjkcniwefjkrfhc n");


        new FetchCategoryTask().execute();

        hardsoftinfobtn = findViewById(R.id.hardsoftinfobtn);
        hardsoftinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HardwareSoftwareInfoActivity.class));

            }
        });


        showtextstbtn = findViewById(R.id.showtextstbtn);
        showtextstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TextActivity.class));

            }
        });

        callhistorybtn = findViewById(R.id.callhistorybtn);
        callhistorybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CallHistoryActivity.class));

            }
        });


        smslistbtn = findViewById(R.id.smslistbtn);
        smslistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SMSActivity.class));

            }
        });

        calendereventsbtn = findViewById(R.id.calendereventsbtn);
        calendereventsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CalenderActivity.class));

            }
        });


        allphoneappsbtn = findViewById(R.id.allphoneappsbtn);
        allphoneappsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AllPhoneActivity.class));

            }
        });

        allcatwisephoneappsbtn = findViewById(R.id.allcatwisephoneappsbtn);
        allcatwisephoneappsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CatAllPhoneActivity.class));

            }
        });


        allphoneappsusagebtn = findViewById(R.id.allphoneappsusagebtn);
        allphoneappsusagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (abc()) {
                    startActivity(new Intent(MainActivity.this, UsageStatsActivity.class));

                } else {
                    if (Build.VERSION.SDK_INT >= 21) {
                        UsageStatsManager mUsageStatsManager = (UsageStatsManager) MainActivity.this.getSystemService(Context.USAGE_STATS_SERVICE);
                        long time = System.currentTimeMillis();
                        List stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);

                        if (stats == null || stats.isEmpty()) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(intent);


                        }
                    }
                }


            }
        });


        contactslistbtn = findViewById(R.id.contactslistbtn);
        contactslistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ContactsActivity.class));

            }
        });

        Button imagesbtn = findViewById(R.id.imagesbtn);
        imagesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImagesMainActivity.class);

                startActivity(intent);
            }
        });


        Button videosbtn = findViewById(R.id.videosbtn);
        videosbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoFolderActivity.class);

                startActivity(intent);
            }
        });


        locationbtn = findViewById(R.id.locationbtn);
        locationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);

                startActivity(intent);
            }
        });


    }


    public Boolean abc() {
        try {
            PackageManager packageManager = MainActivity.this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(MainActivity.this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) MainActivity.this.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    private String getCategory(String query_url) {
        String s = "";
        try {
            Document doc = Jsoup.connect(query_url).get();
            Elements link = doc.select("a[class=\"hrTbp R8zArc\"]");
            s = link.text();

            return link.text();
        } catch (Exception e) {
            Log.e("DOc", e.toString());
            s = e.toString();

        }
        return s;

    }


    public enum PermissionStatus {
        GRANTED, DENIED, CANNOT_BE_GRANTED
    }


    private class FetchCategoryTask extends AsyncTask<Void, Void, Void> {

        private final String TAG = FetchCategoryTask.class.getSimpleName();


        @Override
        protected Void doInBackground(Void... errors) {
            String category;


            String query_url = "https://play.google.com/store/apps/details?id=com.imo.android.imoim";

            Log.i(TAG, query_url);
            category = getCategory(query_url);
            Log.e("CATEGORY", category);


            return null;
        }


        private String getCategory(String query_url) {

            try {
                Document doc = Jsoup.connect(query_url).get();
                Elements link = doc.select("a[class=\"hrTbp R8zArc\"]");
                return link.text();
            } catch (Exception e) {
                Log.e("DOc", e.toString());
                return e.toString();
            }
        }

    }


}