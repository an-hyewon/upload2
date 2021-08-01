package com.example.upload2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button activity2,activity3, activity4, activity5, activity6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity2 = findViewById(R.id.button2);
        activity3 = findViewById(R.id.button3);
        activity4 = findViewById(R.id.button4);
        activity5 = findViewById(R.id.button5);
        activity6 = findViewById(R.id.button5);

        activity2.setOnClickListener(this);
        activity3.setOnClickListener(this);
        activity4.setOnClickListener(this);
        activity5.setOnClickListener(this);
        activity6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent2;
        switch (v.getId()){
            case R.id.button2:
                intent2 = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent2);
                break;
            case R.id.button3:
                intent2 = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent2);
                break;
            case R.id.button4:
                intent2 = new Intent(MainActivity.this, MainActivity4.class);
                startActivity(intent2);
                break;
            case R.id.button5:
                intent2 = new Intent(MainActivity.this, MainActivity5.class);
                startActivity(intent2);
                break;
            case R.id.button6:
                intent2 = new Intent(MainActivity.this, MainActivity6.class);
                startActivity(intent2);
                break;
        }
    }
}
