package com.example.upload2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upload2.utils.FileUtil2;
import com.example.upload2.utils.FileUtil3;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity5 extends AppCompatActivity {
    private ArrayList<String> imageListUri = new ArrayList<>();
    private ArrayList<Bitmap> mBitmap = new ArrayList<>();

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView4);
    }

    public void takePermissions(View v){
        if (isPermissonsGrated()){
            Toast.makeText(this,"Permission Already Granted",Toast.LENGTH_SHORT).show();
        } else {
            takePermission();
        }
    }

    public void pickFromGallery(View v){
        if (isPermissonsGrated()){
            pickImageFromGallery();
        } else {
            takePermission();
        }

    }

    public void multipartImageUpload(View v){
        try {
            Retrofit retrofit = RetrofitClient.getClient();
            ServiceApi retrofitAPI = retrofit.create(ServiceApi.class);

            List<MultipartBody.Part> files = new ArrayList<>();
            long user_id = 123L;
            for (int i=0;i<imageListUri.size();i++){
                Log.d("imageListUri", imageListUri.get(i));

                File file = new File(imageListUri.get(i));

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mBitmap.get(i).compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("upload", file.getName(), fileBody);
                files.add(filePart);
            }
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user_id));

            Call<ResponseBody> req = retrofitAPI.imageTest(files, id);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Log.i("response","success! code : "+response.code());
                        Log.e("body",response.body().toString());
                    }
                    Toast.makeText(getBaseContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("response","fail");
                    t.printStackTrace();
                }
            });

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isPermissonsGrated(){
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            //For Android 11
            return Environment.isExternalStorageManager();
        } else {
            //For Below
            int readExternalStoragePermisson = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            return readExternalStoragePermisson == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void takePermission(){
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent,100);
            } catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,100);
            }
        } else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }
    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent,102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 100){
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
                    if (Environment.isExternalStorageManager()){
                        Toast.makeText(this,"Permission Granted in android 11",Toast.LENGTH_SHORT).show();
                        pickImageFromGallery();
                    } else {
                        takePermission();
                    }
                }
            } else if(requestCode == 102){
                if (data != null && data.getData() != null){
                    Uri uri = data.getData();
                    String path = FileUtil3.getPath(this, uri);
                    imageListUri.add(path);
                    mBitmap.add(BitmapFactory.decodeFile(path));
                    File f1 = new File(path);
                    Log.e("imagePath", "단일 : "+path+"\n" +"size : "+f1.length());
                } else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ClipData clipData = data.getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                ClipData.Item item = clipData.getItemAt(i);
                                Uri uri = item.getUri();
                                Log.e("tempUri", "다중 : "+uri.toString());
                                String path = FileUtil3.getPath(this, uri);
                                imageListUri.add(path);
                                mBitmap.add(BitmapFactory.decodeFile(path));
                                File f1 = new File(path);
                                Log.e("imagePath", "단일 : "+path+"\n" +"size : "+f1.length());

                            }
                            // Do someting
                        } else {
                            Log.e("clipdata","null");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0){
            if (requestCode == 101){
                boolean readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (readExternalStorage){
                    Toast.makeText(this,"Read Permission is Granted in android 10 or below",Toast.LENGTH_SHORT).show();
                    pickImageFromGallery();
                } else {
                    takePermission();
                }
            }
        }
    }
}