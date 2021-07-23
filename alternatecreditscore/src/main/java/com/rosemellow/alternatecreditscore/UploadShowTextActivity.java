package com.rosemellow.alternatecreditscore;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadShowTextActivity extends AppCompatActivity {

    Button uploadtoserverbtn;
    RetrofitService retrofitService;

    TextView texttv;

    LinearLayout loaderlayout;

    public void showLoader() {
        loaderlayout.setVisibility(View.VISIBLE);
        loaderlayout.setClickable(true);
    }

    public void hideLoader() {
        loaderlayout.setVisibility(View.GONE);
        loaderlayout.setClickable(false);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);
        retrofitService = ApiBaseUrl.getClient().create(RetrofitService.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle(getIntent().getStringExtra("title"));


        loaderlayout = findViewById(R.id.loaderlayout);
        hideLoader();

        texttv = findViewById(R.id.texttv);
        texttv.setText(StartCommonFetchingActivity.sbfinal.toString());

        uploadtoserverbtn = findViewById(R.id.uploadtoserverbtn);

        if (getIntent().getStringExtra("title").equalsIgnoreCase("Upload")) {
            uploadtoserverbtn.setVisibility(View.VISIBLE);
        } else {
            uploadtoserverbtn.setVisibility(View.GONE);
        }
        uploadtoserverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();

            }
        });

    }

    private void uploadFile() {

        File imagefile = generateNoteOnSD(UploadShowTextActivity.this,
                "PHONEDETAILS.tsv", StartCommonFetchingActivity.sbfinal.toString());

        RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), imagefile);

        MultipartBody.Part partImage = MultipartBody.Part.createFormData("csv_file",
                new SavePref(UploadShowTextActivity.this).getMyPhoneNumber()+"_file" + ".tsv", image);

        showLoader();
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
                    Toast.makeText(UploadShowTextActivity.this, "Uploaded Text File Link to Clipboard", Toast.LENGTH_SHORT).show();

                    Toast.makeText(UploadShowTextActivity.this, "" + registerDetailModel.getMsg(), Toast.LENGTH_SHORT).show();
                    if (registerDetailModel.getSuccess().equalsIgnoreCase("1")) {

                        startActivity(new Intent(UploadShowTextActivity.this, FinalActivity.class));

                        finish();
                    } else {
                    }

                    hideLoader();

                } catch (Exception e) {
                    Log.e("Fewcxesdxedsab", e.getMessage());
                    hideLoader();
                }


            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                Log.e("Fewcxesdxeds", t.getMessage());
                t.printStackTrace();
                hideLoader();
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


}