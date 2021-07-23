package com.rosemellow.alternatecreditscore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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