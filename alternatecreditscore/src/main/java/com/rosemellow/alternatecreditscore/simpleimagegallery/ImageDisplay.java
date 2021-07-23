package com.rosemellow.alternatecreditscore.simpleimagegallery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.opencsv.CSVWriter;
import com.rosemellow.alternatecreditscore.BaseActivity;
import com.rosemellow.alternatecreditscore.R;
import com.rosemellow.alternatecreditscore.simpleimagegallery.fragments.pictureBrowserFragment;
import com.rosemellow.alternatecreditscore.simpleimagegallery.utils.MarginDecoration;
import com.rosemellow.alternatecreditscore.simpleimagegallery.utils.PicHolder;
import com.rosemellow.alternatecreditscore.simpleimagegallery.utils.itemClickListener;
import com.rosemellow.alternatecreditscore.simpleimagegallery.utils.pictureFacer;
import com.rosemellow.alternatecreditscore.simpleimagegallery.utils.picture_Adapter;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Author CodeBoy722
 * <p>
 * This Activity get a path to a folder that contains images from the MainActivity Intent and displays
 * all the images in the folder inside a RecyclerView
 */

public class ImageDisplay extends BaseActivity implements itemClickListener {

    RecyclerView imageRecycler;
    ArrayList<pictureFacer> allpictures;


    String foldePath;
    TextView folderName;

    Button exportbtn;

    ImageDB imageDB;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle(getIntent().getStringExtra("folderName"));

        imageDB = new ImageDB(ImageDisplay.this);


        foldePath = getIntent().getStringExtra("folderPath");
        allpictures = new ArrayList<>();
        imageRecycler = findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(this));
        imageRecycler.hasFixedSize();


        if (allpictures.isEmpty()) {

            allpictures = getAllImagesByFolder(foldePath);
            imageRecycler.setAdapter(new picture_Adapter(allpictures, ImageDisplay.this, this));

        } else {

        }


        exportbtn = findViewById(R.id.exportbtn);
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exportDB();

            }
        });

    }


    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<pictureFacer> pics) {
        //pictureBrowserFragment browser = pictureBrowserFragment.newInstance( pics, position, ImageDisplay.this);

        // Note that we need the API version check here because the actual transition classes (e.g. Fade)
        // are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
        // ARE available in the support library (though they don't do anything on API < 21)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //browser.setEnterTransition(new Slide());
            //browser.setExitTransition(new Slide()); uncomment this to use slide transition and comment the two lines below
           // browser.setEnterTransition(new Fade());
           // browser.setExitTransition(new Fade());
        }

      /*  getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.picture, position + "picture")
                .add(R.id.displayContainer, browser)
                .addToBackStack(null)
                .commit();*/

    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {

    }


    public ArrayList<pictureFacer> getAllImagesByFolder(String path) {
        ArrayList<pictureFacer> images = new ArrayList<>();
        Uri allVideosuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = ImageDisplay.this.getContentResolver().query(allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursor.moveToFirst();
            do {
                pictureFacer pic = new pictureFacer();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            } while (cursor.moveToNext());
            cursor.close();
            ArrayList<pictureFacer> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > -1; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }


    private void exportDB() {


        ImageDB dbhelper = new ImageDB(ImageDisplay.this);
                File exportDir =new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))) ;
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "Images_" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILDBImage", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {

                String[] arrStr = {curCSV.getString(0) + "",
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3)};
                csvWrite.writeNext(arrStr);


            }
            csvWrite.close();
            curCSV.close();

            Toast.makeText(this, "CSV Stored in Downloads", Toast.LENGTH_SHORT).show();

        } catch (Exception sqlEx) {
            Log.e("DisplayDbMainActivity", sqlEx.getMessage(), sqlEx);
        }
    }


}
