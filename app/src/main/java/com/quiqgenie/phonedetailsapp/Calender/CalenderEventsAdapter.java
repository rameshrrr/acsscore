package com.quiqgenie.phonedetailsapp.Calender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quiqgenie.phonedetailsapp.R;

import java.util.ArrayList;

public class CalenderEventsAdapter extends RecyclerView.Adapter<CalenderEventsAdapter.ViewHolder> {


    public ArrayList<CalenderEventsModel> arrayList;
    Context context;

    public CalenderEventsAdapter(Context context, ArrayList<CalenderEventsModel> arrayList) {
        try {
            this.context = context;
            this.arrayList = arrayList;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_item_calender, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {

            CalenderEventsModel callPhoneModel = arrayList.get(position);


            holder.eventdesctv.setText(callPhoneModel.getDesc());
            holder.datetv.setText(callPhoneModel.getDaye());
            holder.eventtitle.setText(callPhoneModel.getEventName());


        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView datetv;
        TextView eventdesctv;
        TextView eventtitle;


        public ViewHolder(View v) {
            super(v);
            eventdesctv = v.findViewById(R.id.eventdesctv);
            eventtitle = v.findViewById(R.id.eventtitle);
            datetv = v.findViewById(R.id.datetv);

        }
    }
}