package com.quiqgenie.phonedetailsapp;

import android.content.Context;
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

public class ShowTextActivity extends AppCompatActivity {

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
        texttv.setText(getIntent().getStringExtra("text"));

        uploadtoserverbtn = findViewById(R.id.uploadtoserverbtn);
        uploadtoserverbtn.setVisibility(View.GONE);


    }





}