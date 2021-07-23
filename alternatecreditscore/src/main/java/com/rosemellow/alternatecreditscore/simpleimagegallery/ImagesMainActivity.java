package com.rosemellow.alternatecreditscore.simpleimagegallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.opencsv.CSVWriter;
import com.rosemellow.alternatecreditscore.BaseActivity;
import com.rosemellow.alternatecreditscore.R;
import com.rosemellow.alternatecreditscore.SavePref;
import com.rosemellow.alternatecreditscore.simpleimagegallery.utils.MarginDecoration;
import com.rosemellow.alternatecreditscore.simpleimagegallery.utils.PicHolder;
import com.rosemellow.alternatecreditscore.simpleimagegallery.utils.imageFolder;
import com.rosemellow.alternatecreditscore.simpleimagegallery.utils.itemClickListener;
import com.rosemellow.alternatecreditscore.simpleimagegallery.utils.pictureFacer;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ImagesMainActivity extends BaseActivity implements itemClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    RecyclerView folderRecycler;

    ImageFolderDB imageFolderDB;

    Button exportbtn;
    StringBuffer sb = new StringBuffer();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle("Images");


        sb.append("FOLDER WISE IMAGES" + "\n\n");


        sb.append("Folder Name,  No. of Images " + "\n\n");


        imageFolderDB = new ImageFolderDB(ImagesMainActivity.this);
        imageFolderDB.clearDB();

        if (ContextCompat.checkSelfPermission(ImagesMainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(ImagesMainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


        folderRecycler = findViewById(R.id.folderRecycler);
        folderRecycler.addItemDecoration(new MarginDecoration(this));
        folderRecycler.hasFixedSize();
        ArrayList<imageFolder> folds = getPicturePaths();

        if (folds.isEmpty()) {


        } else {
            RecyclerView.Adapter folderAdapter = new pictureFolderAdapter(folds,
                    ImagesMainActivity.this, this);
            folderRecycler.setAdapter(folderAdapter);
        }


        exportbtn = findViewById(R.id.exportbtn);
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exportDB();

            }
        });

    }


    private ArrayList<imageFolder> getPicturePaths() {
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
            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPics());
        }


        return picFolders;
    }


    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<pictureFacer> pics) {

    }

    /**
     * Each time an item in the RecyclerView is clicked this method from the implementation of the transitListerner
     * in this activity is executed, this is possible because this class is passed as a parameter in the creation
     * of the RecyclerView's Adapter, see the adapter class to understand better what is happening here
     *
     * @param pictureFolderPath a String corresponding to a folder path on the device external storage
     */
    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {
        Intent move = new Intent(ImagesMainActivity.this, ImageDisplay.class);
        move.putExtra("folderPath", pictureFolderPath);
        move.putExtra("folderName", folderName);

        //move.putExtra("recyclerItemSize",getCardsOptimalWidth(4));
        startActivity(move);
    }


    /**
     * Default status bar height 24dp,with code API level 24
     */


    private void exportDB() {


        ImageFolderDB dbhelper = new ImageFolderDB(ImagesMainActivity.this);
        File exportDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "ImageFolderVideoList_" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILDBImageFolder", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {

                String[] arrStr = {curCSV.getString(0) + "",
                        curCSV.getString(1),
                        curCSV.getString(2)};
                csvWrite.writeNext(arrStr);


            }
            csvWrite.close();
            curCSV.close();

            Toast.makeText(this, "CSV Stored in Downloads", Toast.LENGTH_SHORT).show();

        } catch (Exception sqlEx) {
            Log.e("DisplayDbMainActivity", sqlEx.getMessage(), sqlEx);
        }
    }


    public class pictureFolderAdapter extends RecyclerView.Adapter<pictureFolderAdapter.FolderHolder> {

        ImageFolderDB imageFolderDB;
        private ArrayList<imageFolder> folders;
        private Context folderContx;
        private itemClickListener listenToClick;


        /**
         * @param folders     An ArrayList of String that represents paths to folders on the external storage that contain pictures
         * @param folderContx The Activity or fragment Context
         * @param listen      interFace for communication between adapter and fragment or activity
         */
        public pictureFolderAdapter(ArrayList<imageFolder> folders, Context folderContx, itemClickListener listen) {
            this.folders = folders;
            this.folderContx = folderContx;
            this.listenToClick = listen;
            this.imageFolderDB = new ImageFolderDB(folderContx);

        }

        @NonNull
        @Override
        public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View cell = inflater.inflate(R.layout.picture_folder_item, parent, false);
            return new FolderHolder(cell);

        }

        @Override
        public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
            final imageFolder folder = folders.get(position);

            Glide.with(folderContx)
                    .load(folder.getFirstPic())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.folderPic);

            //setting the number of images
            String text = "" + folder.getFolderName();
            String folderSizeString = "" + folder.getNumberOfPics() + " Media";
            holder.folderSize.setText(folderSizeString);
            holder.folderName.setText(text);

            holder.folderPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenToClick.onPicClicked(folder.getPath(), folder.getFolderName());
                }
            });

            imageFolderDB.addMoods(new ImageFolderModel(1, text, folderSizeString + ""));

            sb.append(text + ",  " +
                    folderSizeString + "  " +
                    "\n");


            new SavePref(ImagesMainActivity.this).setImagesFolders(sb.toString());


        }

        @Override
        public int getItemCount() {
            return folders.size();
        }


        public class FolderHolder extends RecyclerView.ViewHolder {
            ImageView folderPic;
            TextView folderName;

            TextView folderSize;

            CardView folderCard;

            public FolderHolder(@NonNull View itemView) {
                super(itemView);
                folderPic = itemView.findViewById(R.id.folderPic);
                folderName = itemView.findViewById(R.id.folderName);
                folderSize = itemView.findViewById(R.id.folderSize);
                folderCard = itemView.findViewById(R.id.folderCard);
            }
        }

    }


}
