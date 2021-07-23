package com.quiqgenie.phonedetailsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TextActivity extends AppCompatActivity {
    public static StringBuffer sb = new StringBuffer();
    Button hardsoftinfobtn;
    Button locationbtn;
    Button uploadbtn;
    Button callhistorybtn;
    Button smslistbtn;
    Button calendereventsbtn;
    Button allphoneappsbtn;
    Button allcatwisephoneappsbtn;
    Button allphoneappsusagebtn;
    Button contactslistbtn;
    Button imagesbtn;
    Button videosbtn;
    RetrofitService retrofitService;
    Boolean b = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        retrofitService = ApiBaseUrl.getClient().create(RetrofitService.class);


        sb.append("PHONE APP DETAILS" + "\n\n\n");


        hardsoftinfobtn = findViewById(R.id.hardsoftinfobtn);
        if (new SavePref(this).getHardSoftInfo().equalsIgnoreCase("")) {
            hardsoftinfobtn.setVisibility(View.GONE);
        } else {
            b = true;
            hardsoftinfobtn.setVisibility(View.VISIBLE);
            sb.append(new SavePref(this).getHardSoftInfo() + "\n\n");
        }


        hardsoftinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendTextIntent(new SavePref(TextActivity.this).getHardSoftInfo(), hardsoftinfobtn.getText().toString());
            }
        });

        callhistorybtn = findViewById(R.id.callhistorybtn);

        if (new SavePref(this).getCallLogData().equalsIgnoreCase("")) {
            callhistorybtn.setVisibility(View.GONE);
        } else {
            callhistorybtn.setVisibility(View.VISIBLE);
            b = true;
            sb.append(new SavePref(this).getCallLogData() + "\n\n");
        }


        callhistorybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendTextIntent(new SavePref(TextActivity.this).getCallLogData(), callhistorybtn.getText().toString());
            }
        });


        smslistbtn = findViewById(R.id.smslistbtn);
        if (new SavePref(this).getSMSData().equalsIgnoreCase("")) {
            smslistbtn.setVisibility(View.GONE);
        } else {
            smslistbtn.setVisibility(View.VISIBLE);
            b = true;
            sb.append(new SavePref(this).getSMSData() + "\n\n");
        }
        smslistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextIntent(new SavePref(TextActivity.this).getSMSData(), smslistbtn.getText().toString());

            }
        });

        calendereventsbtn = findViewById(R.id.calendereventsbtn);

        if (new SavePref(this).getCalenderEvents().equalsIgnoreCase("")) {
            calendereventsbtn.setVisibility(View.GONE);
        } else {
            calendereventsbtn.setVisibility(View.VISIBLE);
            b = true;
            sb.append(new SavePref(this).getCalenderEvents() + "\n\n");
        }
        calendereventsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextIntent(new SavePref(TextActivity.this).getCalenderEvents(), calendereventsbtn.getText().toString());

            }
        });


        allphoneappsbtn = findViewById(R.id.allphoneappsbtn);

        if (new SavePref(this).getInstalledApps().equalsIgnoreCase("")) {
            allphoneappsbtn.setVisibility(View.GONE);
        } else {
            allphoneappsbtn.setVisibility(View.VISIBLE);
            b = true;
            sb.append(new SavePref(this).getInstalledApps() + "\n\n");
        }
        allphoneappsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendTextIntent(new SavePref(TextActivity.this).getInstalledApps(), allphoneappsbtn.getText().toString());
            }
        });

        allcatwisephoneappsbtn = findViewById(R.id.allcatwisephoneappsbtn);
        if (new SavePref(this).getCatWiseApps().equalsIgnoreCase("")) {
            allcatwisephoneappsbtn.setVisibility(View.GONE);
        } else {
            b = true;
            allcatwisephoneappsbtn.setVisibility(View.VISIBLE);
            sb.append(new SavePref(this).getCatWiseApps() + "\n\n");
        }
        allcatwisephoneappsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextIntent(new SavePref(TextActivity.this).getCatWiseApps(), allcatwisephoneappsbtn.getText().toString());

            }
        });


        allphoneappsusagebtn = findViewById(R.id.allphoneappsusagebtn);
        if (new SavePref(this).getAppUsage().equalsIgnoreCase("")) {
            allphoneappsusagebtn.setVisibility(View.GONE);
        } else {
            allphoneappsusagebtn.setVisibility(View.VISIBLE);
            b = true;
            sb.append(new SavePref(this).getAppUsage() + "\n\n");
        }
        allphoneappsusagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextIntent(new SavePref(TextActivity.this).getAppUsage(), allphoneappsusagebtn.getText().toString());

            }
        });


        contactslistbtn = findViewById(R.id.contactslistbtn);
        if (new SavePref(this).getContactsList().equalsIgnoreCase("")) {
            contactslistbtn.setVisibility(View.GONE);
        } else {
            b = true;
            contactslistbtn.setVisibility(View.VISIBLE);
            sb.append(new SavePref(this).getContactsList() + "\n\n");
        }
        contactslistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextIntent(new SavePref(TextActivity.this).getContactsList(), contactslistbtn.getText().toString());
            }
        });

        imagesbtn = findViewById(R.id.imagesbtn);

        if (new SavePref(this).getImagesFolders().equalsIgnoreCase("")) {
            imagesbtn.setVisibility(View.GONE);
        } else {
            imagesbtn.setVisibility(View.VISIBLE);
            b = true;
            sb.append(new SavePref(this).getImagesFolders() + "\n\n");
        }
        imagesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextIntent(new SavePref(TextActivity.this).getImagesFolders(), imagesbtn.getText().toString());
            }
        });


        videosbtn = findViewById(R.id.videosbtn);
        if (new SavePref(this).getVideosFolders().equalsIgnoreCase("")) {
            videosbtn.setVisibility(View.GONE);
        } else {
            videosbtn.setVisibility(View.VISIBLE);
            b = true;
            sb.append(new SavePref(this).getVideosFolders() + "\n\n");
        }
        videosbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextIntent(new SavePref(TextActivity.this).getVideosFolders(), videosbtn.getText().toString());

            }
        });


        locationbtn = findViewById(R.id.locationbtn);
        if (new SavePref(this).getLocation().equalsIgnoreCase("")) {
            locationbtn.setVisibility(View.GONE);
        } else {
            b = true;
            locationbtn.setVisibility(View.VISIBLE);
            sb.append(new SavePref(this).getLocation() + "\n\n");
        }
        locationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextIntent(new SavePref(TextActivity.this).getLocation(), locationbtn.getText().toString());
            }
        });


        uploadbtn = findViewById(R.id.uploadbtn);
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  sendTextIntent(sb.toString(), "Upload");

                uploadFile();*/

                if (b) {
                    startActivity(new Intent(TextActivity.this, UploadShowTextActivity.class)
                            .putExtra("title", "Upload"));

                } else {
                    Toast.makeText(TextActivity.this, "No Data to Upload", Toast.LENGTH_SHORT).show();
                }



/*

                generateNoteOnSD(TextActivity.this,
                        "Datas.txt",new SavePref(TextActivity.this).getHardSoftInfo());
*/

            }
        });


    }

    public void sendTextIntent(String text, String title) {
        startActivity(new Intent(TextActivity.this, ShowTextActivity.class)
                .putExtra("text", text)
                .putExtra("title", title));


    }


}