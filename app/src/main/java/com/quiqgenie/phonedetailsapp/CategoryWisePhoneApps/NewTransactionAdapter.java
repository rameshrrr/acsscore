package com.quiqgenie.phonedetailsapp.CategoryWisePhoneApps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quiqgenie.phonedetailsapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewTransactionAdapter extends RecyclerView.Adapter<NewTransactionAdapter.ViewHolder> {

    Context context;
    HashMap<String, ArrayList<CatAppInfo>> arrayList;

    CatAllAppsDB catAllAppsDB;


    public NewTransactionAdapter( Context context, HashMap<String, ArrayList<CatAppInfo>> arrayList) {
        try {
            this.context = context;
            this.arrayList = arrayList;
            this.catAllAppsDB = new CatAllAppsDB(context);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_item_transaction_new, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        int i = 0;
        for (Map.Entry<String, ArrayList<CatAppInfo>> entry : arrayList.entrySet()) {
            if (position == i) {
                String key = entry.getKey();
                ArrayList<CatAppInfo> value = entry.getValue();
                holder.branchnametv.setText(key + " (" + value.size() + ")");

                for (int k=0;k<value.size();k++)
                {
                    CatAppInfo catAppInfo=value.get(k);

                    catAllAppsDB.addMoods(new DBCatAppInfo(1,
                            catAppInfo.getAppname(),
                            catAppInfo.getPname(),
                            catAppInfo.getVersionName(),
                            catAppInfo.getCategoryName()
                    ));

                }



                holder.recyclerView.setAdapter(new TransactionAdapter(context, value));


                break;
            }
            i++;
        }


        holder.recyclerView.setVisibility(View.GONE);


        holder.branchnametv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.recyclerView.getVisibility() == View.VISIBLE) {
                    holder.recyclerView.setVisibility(View.GONE);
                } else {
                    holder.recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView branchnametv;
        RecyclerView recyclerView;


        public ViewHolder(View v) {
            super(v);
            branchnametv = v.findViewById(R.id.branchnametv);
            recyclerView = v.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        }
    }
}
