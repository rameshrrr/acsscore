package com.quiqgenie.phonedetailsapp.SmsModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quiqgenie.phonedetailsapp.R;

import java.util.ArrayList;

public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.ViewHolder> {


    public ArrayList<SmsListModel> arrayList;
    Context context;

    public SMSAdapter(Context context, ArrayList<SmsListModel> arrayList) {
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
        View v = inflater.inflate(R.layout.single_item_sms, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {

            SmsListModel callPhoneModel = arrayList.get(position);


            holder.timetv.setText(callPhoneModel.getSmsDate());
            holder.datetv.setText(callPhoneModel.getTime());
            holder.smstypetv.setText(callPhoneModel.getType());
            holder.phonenumbertv.setText(callPhoneModel.getNumber());
            holder.smsbodytv.setText(callPhoneModel.getSmsData());


        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView timetv;
        TextView datetv;
        TextView smstypetv;
        TextView phonenumbertv;
        TextView smsbodytv;


        public ViewHolder(View v) {
            super(v);
            smsbodytv = v.findViewById(R.id.smsbodytv);
            timetv = v.findViewById(R.id.timetv);
            datetv = v.findViewById(R.id.datetv);
            smstypetv = v.findViewById(R.id.smstypetv);
            phonenumbertv = v.findViewById(R.id.phonenumbertv);
        }
    }
}