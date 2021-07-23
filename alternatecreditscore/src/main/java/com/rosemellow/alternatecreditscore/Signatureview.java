package com.rosemellow.alternatecreditscore;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.kyanogen.signatureview.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Signatureview extends AppCompatActivity {

    private SignatureView signatureView;
    Button sub1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);//see xml layout
        signatureView = findViewById(R.id.signature_view);
        ImageView clear = (ImageView) findViewById(R.id.clear);
        sub1=findViewById(R.id.sub1);
        int colorPrimary = ContextCompat.getColor(this, R.color.colorAccent);
        signatureView.setPenColor(colorPrimary);
        // or like signatureView.setPenColor(Color.RED);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clearCanvas();
            }
        });
        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (abc()) {
                    startActivity(new Intent(Signatureview.this,
                            StartCommonFetchingActivity.class));
                } else {
                    if (Build.VERSION.SDK_INT >= 21) {
                        UsageStatsManager mUsageStatsManager = (UsageStatsManager) Signatureview.this.getSystemService(Context.USAGE_STATS_SERVICE);
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
            PackageManager packageManager = Signatureview.this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(Signatureview.this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) Signatureview.this.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signature, menu);
        return true;
    }*/
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_clear:
                signatureView.clearCanvas();//Clear SignatureView
                Toast.makeText(getApplicationContext(),
                        "Clear", Toast.LENGTH_SHORT).show();
                return true;
           *//* case R.id.action_download:
                File directory = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(directory, System.currentTimeMillis() + ".png");
                FileOutputStream out = null;
                Bitmap bitmap = signatureView.getSignatureBitmap();
                try {
                    out = new FileOutputStream(file);
                    if (bitmap != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    } else {
                        throw new FileNotFoundException();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.flush();
                            out.close();

                            if (bitmap != null) {
                                Toast.makeText(getApplicationContext(),
                                        "Image saved successfully at " + file.getPath(),
                                        Toast.LENGTH_LONG).show();
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                    new MyMediaScanner(this, file);
                                } else {
                                    ArrayList<String> toBeScanned = new ArrayList<String>();
                                    toBeScanned.add(file.getAbsolutePath());
                                    String[] toBeScannedStr = new String[toBeScanned.size()];
                                    toBeScannedStr = toBeScanned.toArray(toBeScannedStr);
                                    MediaScannerConnection.scanFile(this, toBeScannedStr, null,
                                            null);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            case R.id.action_has_signature:

                return true;*//*
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

   /* private class MyMediaScanner implements
            MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mSC;
        private File file;

        MyMediaScanner(Context context, File file) {
            this.file = file;
            mSC = new MediaScannerConnection(context, this);
            mSC.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            mSC.scanFile(file.getAbsolutePath(), null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mSC.disconnect();
        }
    }

  *//*  public void InfoDialog() {
        String infoMessage = "App version : " + BuildConfig.VERSION_NAME;
        infoMessage = infoMessage + "\n\n" + "SignatureView library version : " +
                com.kyanogen.signatureview.BuildConfig.VERSION_NAME;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.vInfo)
                .setMessage(infoMessage)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }*/
}