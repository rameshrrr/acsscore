package com.quiqgenie.phonedetailsapp.VideoModule;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.opencsv.CSVWriter;
import com.quiqgenie.phonedetailsapp.BaseActivity;
import com.quiqgenie.phonedetailsapp.R;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VideoListActivity extends BaseActivity {

    String folderName;

    ArrayList<VideoData> subArrayliat;
    ListView listView;

    RelativeLayout mainrelative;

    Button exportbtn;

    VideoDB videoDB;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle(getIntent().getStringExtra("folderName"));


        folderName = getIntent().getStringExtra("folderName");


        videoDB = new VideoDB(VideoListActivity.this);
        videoDB.clearDB();


        mainrelative = findViewById(R.id.mainrelative);


        subArrayliat = new ArrayList<>();


        listView = findViewById(R.id.video_list);
        listView.setDivider(null);


        for (int i = 0; i < VideoFolderActivity.arrayList.size(); i++) {
            VideoData v = VideoFolderActivity.arrayList.get(i);
            if (v.getData().contains(folderName)) {
                subArrayliat.add(v);


                videoDB.addMoods(new DBVideoData(1, v.getName(),
                        v.getAlbum(),
                        v.getData(),
                        v.getDuration(),
                        v.getDate(),
                        v.getSize()

                ));
            }

        }
        VideoListAdapter adapter = new VideoListAdapter(VideoListActivity.this, subArrayliat);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                VideoData data = subArrayliat.get(position);
                startActivity(new Intent(VideoListActivity.this,
                        GirafferPlayerActivity.class).putExtra("url", data.getData()));

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


    private void exportDB() {


        VideoDB dbhelper = new VideoDB(VideoListActivity.this);
                File exportDir =new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))) ;
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "VideoList_" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILDBVideo", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {

                String[] arrStr = {curCSV.getString(0) + "",
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3),
                        curCSV.getString(4),
                        curCSV.getString(5),
                        curCSV.getString(6)};
                csvWrite.writeNext(arrStr);


            }
            csvWrite.close();
            curCSV.close();

            Toast.makeText(this, "Database Exported  \n path = " + exportDir, Toast.LENGTH_SHORT).show();


        } catch (Exception sqlEx) {
            Log.e("DisplayDbMainActivity", sqlEx.getMessage(), sqlEx);
        }
    }


}