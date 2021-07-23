package com.rosemellow.alternatecreditscore.Contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.opencsv.CSVWriter;
import com.rosemellow.alternatecreditscore.BaseActivity;
import com.rosemellow.alternatecreditscore.R;
import com.rosemellow.alternatecreditscore.SavePref;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactsActivity extends BaseActivity {
    ContactsDB contactsDB;
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
        setContentView(R.layout.activity_contacts);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle("Contacts List");


        contactsDB = new ContactsDB(ContactsActivity.this);
        contactsDB.clearDB();


        sb.append("CONTACTS LIST" + "\n\n");


        sb.append("Name,  Phone Number " + "\n\n");
        ContactsAdapter contactsAdapter = new ContactsAdapter(getContactList());
        RecyclerView recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(ContactsActivity.this));
        recyclerview.setAdapter(contactsAdapter);


        exportbtn = findViewById(R.id.exportbtn);
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exportDB();

            }
        });

    }


    private ArrayList<ContactsModel> getContactList() {

        ArrayList<ContactsModel> contactsModels = new ArrayList<ContactsModel>();

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));


                        Log.i("iduasknzx", "Name: " + name);
                        Log.i("iduasknzx", "Phone Number: " + phoneNo);


                        contactsModels.add(new ContactsModel(name, phoneNo));
                        contactsDB.addMoods(new DBContactsModel(1, name, phoneNo));

                        sb.append(name + ",  " +
                                phoneNo +
                                "\n");


                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }

        new SavePref(this).setContactsList(sb.toString());


        return contactsModels;

    }

    private void exportDB() {


        ContactsDB dbhelper = new ContactsDB(ContactsActivity.this);
                File exportDir =new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))) ;
        exportDir.mkdirs();


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        dateToStr = dateToStr.replace("-", "_");
        dateToStr = dateToStr.replace(":", "_");


        File file = new File(exportDir, "Contacts" + dateToStr + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM PHONEDETAILContacts", null);
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

    public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {


        private List<ContactsModel> categoryDetailModels, secondList;


        public ContactsAdapter(List<ContactsModel> categoryDetailModels) {

            this.categoryDetailModels = categoryDetailModels;

            secondList = new ArrayList<>();
            secondList.addAll(categoryDetailModels);

        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View viewItem = inflater.inflate(R.layout.single_item_contacts, parent, false);
            ViewHolder viewHolder = new ViewHolder(viewItem);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {


            holder.contactsnumbertv.setText(categoryDetailModels.get(position).getNumber());
            holder.contactsusernametv.setText(categoryDetailModels.get(position).getName());


        }

        @Override
        public int getItemCount() {
            return categoryDetailModels.size();
        }


        protected class ViewHolder extends RecyclerView.ViewHolder {

            TextView contactsnumbertv;
            TextView contactsusernametv;
            RelativeLayout linearLayout;


            public ViewHolder(final View v) {
                super(v);

                contactsusernametv = v.findViewById(R.id.contactsusernametv);
                contactsnumbertv = v.findViewById(R.id.contactsnumbertv);
                linearLayout = v.findViewById(R.id.linearLayout);
            }
        }
    }


}