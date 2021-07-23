package com.quiqgenie.phonedetailsapp.VideoModule;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.quiqgenie.phonedetailsapp.R;

import java.util.ArrayList;


public class VideoListAdapter extends BaseAdapter {


    Context context;
    ArrayList<VideoData> arrayList;
    LayoutInflater layoutInflater;


    public VideoListAdapter(Context context, ArrayList<VideoData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.cardimage, parent, false);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final VideoData videoData = arrayList.get(position);

        viewHolder.videosizetv = view.findViewById(R.id.sizetext);

        viewHolder.videonametv = view.findViewById(R.id.textvideos);
        viewHolder.card = view.findViewById(R.id.card);
        viewHolder.videoimageiv = view.findViewById(R.id.imgggview);


        viewHolder.videonametv.setText(videoData.getName());


        viewHolder.videosizetv.setText("" + videoData.getSize());


        Glide.with(context)
                .load(videoData.getData())
                .into(viewHolder.videoimageiv);

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                context.startActivity(new Intent(context, GirafferPlayerActivity.class).putExtra("url", videoData.getData()).putExtra("name", videoData.getName()));

            }
        });


        return view;
    }


    class ViewHolder {


        CardView card;
        TextView videosizetv;
        TextView videonametv;
        ImageView videoimageiv;


    }


}
