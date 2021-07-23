package com.quiqgenie.phonedetailsapp.VideoModule;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;
import com.quiqgenie.phonedetailsapp.BaseActivity;
import com.quiqgenie.phonedetailsapp.R;
import com.quiqgenie.phonedetailsapp.SavePref;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VideoFolderActivity extends BaseActivity {


    public static ArrayList<VideoData> arrayList;
    ListView listView;
    int i, j;
    String subString, folderName;
    ArrayList<String> path;
    VideoData videoData;

    RelativeLayout mainrelative;
    FolderDB folderDB;

    Button exportbtn;
    StringBuffer sb = new StringBuffer();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle("Videos");

        sb.append("FOLDER WISE VIDEOS" + "\n\n");


        sb.append("Folder Name,  No. of Videos " + "\n\n");
        listView = findViewById(R.id.foolderList);


        mainrelative = findViewById(R.id.mainrelative);


        new getVideos(VideoFolderActivity.this).execute();
        path = new ArrayList<>();


        exportbtn = findViewById(R.id.exportbtn);
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exportDB();

            }
        });


    }

    private void exportDB() {


        FolderDB dbhelper = new FolderDB(VideoFolderActivity.this);
        File exportDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "FolderVideoList_" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILDBFolder", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {

                String[] arrStr = {curCSV.getString(0) + "",
                        curCSV.getString(1),
                        curCSV.getString(2)};
                csvWrite.writeNext(arrStr);


            }
            csvWrite.close();
            curCSV.close();

            Toast.makeText(this, "Database Exported  \n path = " + exportDir, Toast.LENGTH_SHORT).show();


        } catch (Exception sqlEx) {
            Log.e("DisplayDbMainActivity", sqlEx.getMessage(), sqlEx);
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
                    videoData = videoDataList.get(i);
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

                FolderVideoListAdapter adapter = new FolderVideoListAdapter(context, path);
                listView.setAdapter(adapter);


            } else {

            }
        }


    }

    public class FolderVideoListAdapter extends BaseAdapter {
        Context context;
        ArrayList<String> path;
        LayoutInflater layoutInflater;


        public FolderVideoListAdapter(Context context, ArrayList<String> path) {
            this.context = context;
            this.path = path;
            layoutInflater = LayoutInflater.from(context);
            folderDB = new FolderDB(context);
            folderDB.clearDB();

        }

        @Override
        public int getCount() {
            return path.size();
        }

        @Override
        public Object getItem(int position) {
            return path.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.cardimagefolders, parent, false);
                viewHolder = new ViewHolder();
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.textvideos = view.findViewById(R.id.textvideos);
            viewHolder.linelayout = view.findViewById(R.id.linelayout);

            viewHolder.videonumberstv = view.findViewById(R.id.videonumberstv);

            viewHolder.folderimageiv = view.findViewById(R.id.folderimageiv);


            viewHolder.videonumberstv.setText("" + getCountStr(path.get(position)));

            folderDB.addMoods(new FolderModel(1, path.get(position), "" + getCountStr(path.get(position))));


            sb.append(path.get(position) + ",  " +
                    getCountStr(path.get(position)) + "  " +
                    "\n");


            new SavePref(VideoFolderActivity.this).setVideosFolders(sb.toString());


            viewHolder.textvideos.setText(path.get(position));

            viewHolder.linelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    context.startActivity(new Intent(context, VideoListActivity.class)
                            .putExtra("folderName", path.get(position)));

/*

                Bundle bundle = new Bundle();
                bundle.putString("folderName", path.get(position));
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment someFragment = new DireWiseVideosFragment();
                someFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, someFragment).addToBackStack(null).commit();
*/

                }
            });

            return view;
        }

        public int getCountStr(String foldername) {

            ArrayList<VideoData> subArrayliat = new ArrayList<>();

            for (int i = 0; i < VideoFolderActivity.arrayList.size(); i++) {
                VideoData v = VideoFolderActivity.arrayList.get(i);
                if (v.getData().contains(foldername)) {
                    subArrayliat.add(v);
                }

            }

            return subArrayliat.size();

        }

        class ViewHolder {

            TextView textvideos;
            TextView videonumberstv;

            ImageView folderimageiv;
            LinearLayout linelayout;


        }
    }


}