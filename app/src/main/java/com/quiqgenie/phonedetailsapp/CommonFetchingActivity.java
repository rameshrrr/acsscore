package com.quiqgenie.phonedetailsapp;

import android.app.AppOpsManager;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.quiqgenie.phonedetailsapp.AppUsage.UsageStatsActivity;
import com.quiqgenie.phonedetailsapp.SmsModule.SMSActivity;

import java.util.List;

public class CommonFetchingActivity extends BaseActivity {


    Button startfetchingbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_fetching);
        startfetchingbtn=findViewById(R.id.startfetchingbtn);
        startfetchingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (abc()) {
                    startActivity(new Intent(CommonFetchingActivity.this,
                            StartCommonFetchingActivity.class));
                } else {
                    if (Build.VERSION.SDK_INT >= 21) {
                        UsageStatsManager mUsageStatsManager = (UsageStatsManager) CommonFetchingActivity.this.getSystemService(Context.USAGE_STATS_SERVICE);
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

    }



    public Boolean abc() {
        try {
            PackageManager packageManager = CommonFetchingActivity.this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(CommonFetchingActivity.this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) CommonFetchingActivity.this.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }
}