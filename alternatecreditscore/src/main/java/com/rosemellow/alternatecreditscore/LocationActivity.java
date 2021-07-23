package com.rosemellow.alternatecreditscore;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends BaseActivity {
    public String latLongStr;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    LocationDB locationDB;

    TextView locationtv, longtv, lattv, locationaddresstv;
    Button exportbtn;
    Button fetchlocationbtn;
    Button fetchlastlocationbtn;


    StringBuffer sb = new StringBuffer();


    Button cleardbbtn;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {


        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle("Location");

        locationDB = new LocationDB(LocationActivity.this);


        sb.append("LOCATION" + "\n");


        sb.append("Address,  Postal Code,  Latitude,  Longitude " + "\n");


        locationtv = findViewById(R.id.locationtv);

        fetchlocationbtn = findViewById(R.id.fetchlocationbtn);

        cleardbbtn = findViewById(R.id.cleardbbtn);
        cleardbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationDB.clearDB();

            }
        });

        longtv = findViewById(R.id.longtv);
        lattv = findViewById(R.id.lattv);
        locationaddresstv = findViewById(R.id.locationaddresstv);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        fetchlocationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });

        fetchlastlocationbtn = findViewById(R.id.fetchlastlocationbtn);
        fetchlastlocationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationDB.getAllMoods().size() == 0) {
                    Toast.makeText(LocationActivity.this, "No Location Found", Toast.LENGTH_SHORT).show();
                } else {
                    final Dialog dialog = new Dialog(LocationActivity.this);
                    dialog.setContentView(R.layout.dialog_last_location);
                    dialog.getWindow().setLayout(-1, -2);

                    TextView dlocationtv, dlongtv, dlattv, dadd, dpincode;


                    dlattv = dialog.findViewById(R.id.dlattv);

                    dlongtv = dialog.findViewById(R.id.dlongtv);
                    dlocationtv = dialog.findViewById(R.id.dlocationtv);


                    dadd = dialog.findViewById(R.id.dadd);
                    dpincode = dialog.findViewById(R.id.dpincode);

                    dlocationtv.setText(locationDB.getAllMoods().get(locationDB.getAllMoods().size() - 1).getLatt() + "," + locationDB.getAllMoods().get(locationDB.getAllMoods().size() - 1).getLongg());
                    dlongtv.setText(locationDB.getAllMoods().get(locationDB.getAllMoods().size() - 1).getLongg());
                    dlattv.setText(locationDB.getAllMoods().get(locationDB.getAllMoods().size() - 1).getLatt());


                    dadd.setText(locationDB.getAllMoods().get(locationDB.getAllMoods().size() - 1).getAddress());


                    dialog.show();
                }

            }
        });


        exportbtn = findViewById(R.id.exportbtn);
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exportDB();

            }
        });


    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latLongStr = location.getLatitude() + "," + location.getLongitude();

                                    String currentDate = new SimpleDateFormat("dd MMM yyyy").format(new Date());
                                    String currentTime = new SimpleDateFormat("hh:mm:ss").format(new Date());

                                    Log.e("efdcxedc", "Here");


                                    locationtv.setText(latLongStr);

                                    longtv.setText(location.getLatitude() + "");
                                    lattv.setText(location.getLongitude() + "");


                                    locationaddresstv.setText(getAddress(location.getLatitude(), location.getLongitude()) + "");


                                    locationDB.addMoods(new LocationModel(1, location.getLatitude() + "," + location.getLongitude(),
                                            currentDate, currentTime, location.getLatitude() +
                                            "", location.getLongitude() + "",
                                            getAddress(location.getLatitude(), location.getLongitude())));


                                    sb.append(getAddress(location.getLatitude(), location.getLongitude()) + ",   " +
                                            location.getLatitude() + ",  " +
                                            location.getLongitude() + ",  " +
                                            "\n");


                                    new SavePref(LocationActivity.this).setLocation(sb.toString());

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissionsLocation();
        }
    }


    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionsLocation() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }


    private void exportDB() {


        LocationDB dbhelper = new LocationDB(LocationActivity.this);
//                File exportDir =new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))) ;
//        File exportDir = new File(Environment.getExternalStorageDirectory(), "Download");
        File exportDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "Location_" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILLocationn", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {

                String[] arrStr = {curCSV.getString(0) + "",
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3),
                        curCSV.getString(4),
                        curCSV.getString(5),
                        curCSV.getString(6),
                        curCSV.getString(7)

                };
                csvWrite.writeNext(arrStr);


            }
            csvWrite.close();
            curCSV.close();

            Toast.makeText(this, "CSV Stored in Downloads", Toast.LENGTH_SHORT).show();

        } catch (Exception sqlEx) {
            Log.e("DisplayDbMainActivity", sqlEx.getMessage(), sqlEx);
        }
    }


    public String getAddress(double lat, double lng) {

        Log.e("EFWdscfdsc", lat + " " + lng);

        String addre = "";


        Geocoder geocoder1;
        List<Address> addresses;
        geocoder1 = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder1.getFromLocation(lat, lng, 10); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0) + " "; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            Log.e("EFWdscfdsc", lat + " " + lng + " " + address);
            return address;

        } catch (Exception e) {

        }


        return addre;
    }


}