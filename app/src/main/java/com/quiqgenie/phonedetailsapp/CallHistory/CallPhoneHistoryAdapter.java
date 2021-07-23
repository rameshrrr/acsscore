package com.quiqgenie.phonedetailsapp.CallHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quiqgenie.phonedetailsapp.R;

import java.util.ArrayList;

public class CallPhoneHistoryAdapter extends RecyclerView.Adapter<CallPhoneHistoryAdapter.ViewHolder> {


    public ArrayList<CallPhoneModel> arrayList;
    Context context;

    public CallPhoneHistoryAdapter(Context context, ArrayList<CallPhoneModel> arrayList) {
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
        View v = inflater.inflate(R.layout.single_item_call_phone, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {

            CallPhoneModel callPhoneModel = arrayList.get(position);

            holder.calldurationtv.setText(callPhoneModel.getPhoneNumber());
            holder.geolocationtv.setText(callPhoneModel.getGeocodeStr());
            holder.timetv.setText(callPhoneModel.getCallDayTime());
            holder.datetv.setText(callPhoneModel.getCallDate());
            holder.calltypetv.setText(callPhoneModel.getCallType());
            holder.phonenumbertv.setText(callPhoneModel.getPhoneNumber());
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView calldurationtv;
        TextView geolocationtv;
        TextView timetv;
        TextView datetv;
        TextView calltypetv;
        TextView phonenumbertv;


        public ViewHolder(View v) {
            super(v);

            calldurationtv = v.findViewById(R.id.calldurationtv);
            geolocationtv = v.findViewById(R.id.geolocationtv);
            timetv = v.findViewById(R.id.timetv);
            datetv = v.findViewById(R.id.datetv);
            calltypetv = v.findViewById(R.id.calltypetv);
            phonenumbertv = v.findViewById(R.id.phonenumbertv);
        }
    }
}