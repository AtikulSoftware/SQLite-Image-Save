package com.example.sqliteimagesave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserList extends AppCompatActivity {

    ListView listView;

    DatabaseHelper databaseHelper;

    List<ListItem> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // পরিচয় করিয়ে দেওয়া হয়েছে ।
        Toolbar toolbar = findViewById(R.id.toolbar);
        listView = findViewById(R.id.listView);

        // database helper কে পরিচয় করিয়ে দেওয়া হয়েছে ।
        databaseHelper = new DatabaseHelper(UserList.this);

        //======================================
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //======================================

        Cursor cursor = databaseHelper.getUserProfile();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            byte[] imgBytes = cursor.getBlob(2);

            // arraylist এর মধ্যে item যুক্ত করা হয়েছে ।
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
            listItems.add(new ListItem(name, bitmap));
        }

        cursor.close();
        databaseHelper.close();

        // Adapter কে পরিচয় করিয়ে দেওয়া হয়েছে এবং listView তে set করে দেওয়া হয়েছে ।
        MyAdapter myAdapter = new MyAdapter(listItems);
        listView.setAdapter(myAdapter);

    } // onCreate method end here ===================

    public class MyAdapter extends BaseAdapter {

        List<ListItem> myList;

        public MyAdapter(List<ListItem> myList) {
            this.myList = myList;
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View myView = layoutInflater.inflate(R.layout.list_item, parent, false);

            // পরিচয় করিয়ে দেওয়া হয়েছে ।
            ImageView imgProfile = myView.findViewById(R.id.imgProfile);
            TextView tvName = myView.findViewById(R.id.tvName);

            // list এ সব set করে দেওয়া হয়েছে ।
            tvName.setText(myList.get(position).getName());
            imgProfile.setImageBitmap(myList.get(position).getImage());

            return myView;
        }
    } // MyAdapter end here ============

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

} // public class end here ==========================