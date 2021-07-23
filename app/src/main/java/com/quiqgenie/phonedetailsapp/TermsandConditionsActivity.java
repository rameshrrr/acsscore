package com.quiqgenie.phonedetailsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import static android.Manifest.permission.READ_SMS;

public class TermsandConditionsActivity extends BaseActivity {

    Button proceedbtn;
    private PopupWindow pw;
    private File file;
    private LinearLayout canvasLL;
    private View view;
    private SignatureView mSignature;
    private Bitmap bitmap;

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";
    private int  RESOLVE_PHONE_NUMBER_HINT = 1000;
    String mobNumber;
     GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsand_conditions);


        proceedbtn = findViewById(R.id.proceedbtn);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        requestHint();

        proceedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

     /*           startActivity(new Intent(TermsandConditionsActivity.this, CommonFetchingActivity.class));
                finish();*/

              /*  if (abc()) {
                    startActivity(new Intent(TermsandConditionsActivity.this,
                            StartCommonFetchingActivity.class));
                } else {
                    if (Build.VERSION.SDK_INT >= 21) {
                        UsageStatsManager mUsageStatsManager = (UsageStatsManager) TermsandConditionsActivity.this.getSystemService(Context.USAGE_STATS_SERVICE);
                        long time = System.currentTimeMillis();
                        List stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);

                        if (stats == null || stats.isEmpty()) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(intent);


                        }
                    }
                }*/
                startActivity(new Intent(TermsandConditionsActivity.this, Signatureview.class));

                //showPopupWindow(view);


            }
        });
    }


    private void requestHint() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        final GoogleApiClient googleApiClient =
                new GoogleApiClient.Builder(TermsandConditionsActivity.this).addApi(Auth.CREDENTIALS_API).build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                googleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    RESOLVE_PHONE_NUMBER_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    public Boolean abc() {
        try {
            PackageManager packageManager = TermsandConditionsActivity.this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(TermsandConditionsActivity.this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) TermsandConditionsActivity.this.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_PHONE_NUMBER_HINT && resultCode == RESULT_OK) {
            Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
            final String phoneNumber = credential.getId();
            new SavePref(this).setMyPhoneNumber(phoneNumber);

            Log.e("rammmkska",new SavePref(this).getMyPhoneNumber());
        }

    }

   /* public void showPopupWindow(final View view) {


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);


        View popupView = inflater.inflate(R.layout.popupwindow, null);
        popupView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim));

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 5, 5);


        Button subButton = (Button) popupView.findViewById(R.id.sub1);
       // canvasLL.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSignature =  (SignatureView) findViewById(R.id.signature_view);
        ImageView clear = (ImageView) popupView.findViewById(R.id.clear);
        //subButton.setOnClickListener(cancel_button_click_listener);

        ImageView close = (ImageView) popupView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {

            public void onClick(View popupView) {
                popupWindow.dismiss();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSignature.clearCanvas();
                mSignature.clearCanvas();
            }
        });
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (abc()) {
                    startActivity(new Intent(TermsandConditionsActivity.this,
                            StartCommonFetchingActivity.class));
                } else {
                    if (Build.VERSION.SDK_INT >= 21) {
                        UsageStatsManager mUsageStatsManager = (UsageStatsManager) TermsandConditionsActivity.this.getSystemService(Context.USAGE_STATS_SERVICE);
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


        //Handler for clicking on the inactive zone of the window

    }*/


}