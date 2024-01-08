package com.example.sqliteimagesave;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView img_profile,pickImage;
    EditText ed_name;
    Button btnInsert;
    Uri imageUri;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // পরিচয় করিয়ে দেওয়া হয়েছে ।
        img_profile = findViewById(R.id.img_profile);
        pickImage = findViewById(R.id.pickImage);
        ed_name = findViewById(R.id.ed_name);
        btnInsert = findViewById(R.id.btnInsert);

        // Database Helper কে পরিচয় করিয়ে দেওয়া হয়েছে ।
        databaseHelper = new DatabaseHelper(MainActivity.this);

        // user থেকে image get করা হয়েছে ।
        pickImage.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImages.launch(intent);
        });

        // data insert করা হয়েছে ।
        btnInsert.setOnClickListener(v -> {
            String Name = ed_name.getText().toString();
            if (Name.isEmpty()){
                ed_name.setError("Enter Your Name");
                return;
            } else if (imageUri == null){
                Toast.makeText(MainActivity.this,"Please select image", Toast.LENGTH_SHORT).show();
            } else {
                boolean result =  databaseHelper.insertData(Name, uriToBitmap(imageUri));
                if (result){
                    Toast.makeText(MainActivity.this,"Data Insert", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,"Data not Insert", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // fab button এর onClick লেখা হয়েছে ।
        findViewById(R.id.fabDetails).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,UserList.class));
        });

    } // onCreate method end here =================================

    ActivityResultLauncher<Intent> pickImages = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        imageUri = result.getData().getData();
                        img_profile.setImageURI(imageUri);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    public Bitmap uriToBitmap(Uri uri){
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return BitmapFactory.decodeStream(inputStream);
    }


} // public class end here ========================================