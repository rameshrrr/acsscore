package com.quiqgenie.phonedetailsapp.CategoryWisePhoneApps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.quiqgenie.phonedetailsapp.R;

import java.util.ArrayList;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    Context context;
    ArrayList<CatAppInfo> arrayList;

    public TransactionAdapter(Context context, ArrayList<CatAppInfo> arrayList) {
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
        View v = inflater.inflate(R.layout.single_item_transaction, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.apppackagenametv.setText(arrayList.get(position).getPname());
        holder.appnametv.setText(arrayList.get(position).getAppname());
        holder.appversionnametv.setText(arrayList.get(position).getVersionName());


        Glide.with(context)
                .load(arrayList.get(position).getIcon())
                .into(holder.appiconiv);




    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView appversionnametv, apppackagenametv, appnametv;
        ImageView appiconiv;


        public ViewHolder(View v) {
            super(v);


            appversionnametv = v.findViewById(R.id.appversionnametv);
            apppackagenametv = v.findViewById(R.id.apppackagenametv);
            appnametv = v.findViewById(R.id.appnametv);
            appiconiv = v.findViewById(R.id.appiconiv);

        }
    }
}
