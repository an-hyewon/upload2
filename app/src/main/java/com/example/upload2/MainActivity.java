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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

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

    private final static String BASE_URL = "http://49.50.172.208:8080/:8080/";

    HashMap<String, RequestBody> map = new HashMap<>();
    String user_id = "abc";
    String user_email = "abc@gmail.co.kr";
    String mediaPath;
    ServiceApi serviceApi;
    Uri picUri, photoUri;
    Uri tempUri = null;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    public final static int REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 10;

    private static final int REQUEST_CAMERA = 100;
    private String cacheFilePath = null;

    ArrayList<Bitmap> mBitmap = new ArrayList<>();
    Bitmap mBitmap1;
    private Button activity, camera, select, upload;
    private TextView textView, textView2;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;

    String[] filePathColumn = { MediaStore.Video.Media.DATA };
    String imagePath = null;
    ArrayList<String> imageListUri = new ArrayList<>();
    ArrayList<Uri> realUri = new ArrayList<>();
    File f1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = findViewById(R.id.button);
        camera = findViewById(R.id.camera);
        select = findViewById(R.id.select);
        upload = findViewById(R.id.upload);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);

//        askPermissions();
//        initRetrofitClient();


        activity.setOnClickListener(this);
        camera.setOnClickListener(this);
        select.setOnClickListener(this);
        upload.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
                break;
            case R.id.camera:
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                break;
            case R.id.select:
                filePicker();
//                if(Build.VERSION.SDK_INT>=23){
//                    if(checkPermission()){
//                        if(Build.VERSION.SDK_INT<29){
//                            filePicker();
//                        } else if(Build.VERSION.SDK_INT==29) {
//                            filePicker2();
//                        } else {
//                            filePicker();
//                        }
//                    } else{
//                        requestPermission();
//                    }
//                } else {
//                    filePicker();
//                }
                break;
            case R.id.upload:
                if (mBitmap != null){
                    for (int i = 0; i < mBitmap.size(); i++){
                        multipartImageUpload(i);
                        FirebaseUpload(i);
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void check(){
        Log.i("state",Environment.getExternalStorageState());
    }



    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Log.d("Permission","Please Give Permission to Upload File");
            Toast.makeText(MainActivity.this,"Please Give Permission to Upload File",Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        serviceApi = new Retrofit.Builder().baseUrl(BASE_URL).client(client).build().create(ServiceApi.class);
    }

    private void filePicker(){
        Log.d("call","filePicker");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Get File"),REQUEST_CODE);
    }

//    private void filePicker2(){
//        Log.d("call","filePicker2");
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        startActivityForResult(intent, REQUEST_CODE);
//    }

    public Intent getPickImageChooserIntent() {
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
            photoUri = outputFileUri;
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_RESULT) {
                photoUri = data.getData();
                imagePath = getImageFilePath(data);
                imageListUri.add(imagePath);
                realUri.add(Uri.parse(imagePath));
                if (imagePath != null) {
                    mBitmap.add(BitmapFactory.decodeFile(imagePath));
                    Log.d("mBitmap",mBitmap.get(0).toString());
                    imageView1.setImageBitmap(mBitmap.get(0));
                    Log.d("imageView1","성공");

                    f1 = new File(imagePath);
                    textView2.setText(textView2.getText().toString()
                            +"\n1.path: "+imagePath+"\n"
                            +"2.name: "+f1.getName()+"\n"
                            +"3.size: "+f1.length()+"\n"
                            +"4.mimeType: "+getContentResolver().getType(photoUri)+"\n");
                }
            } else if (requestCode == REQUEST_CODE) {
                if (data == null){
                    Log.i("data", "null");
                } else {
                    if(data.getClipData() == null){
                        Toast.makeText(getBaseContext(), "다중선택이 불가능한 기기입니다",Toast.LENGTH_SHORT).show();
                    } else{
                        ClipData clipData = data.getClipData();
                        Log.i("clipData", String.valueOf(clipData.getItemCount()));

//                        tempUri = data.getData();
//                        imagePath = getImageFilePath(data);

                        if(clipData.getItemCount() > 5){
                            Toast.makeText(getBaseContext(), "사진은 5장까지 선택 가능",Toast.LENGTH_SHORT).show();

                        } else if(clipData.getItemCount() == 1){
                            tempUri = clipData.getItemAt(0).getUri();
                            imagePath = getPathFromURI(tempUri);
                            imageListUri.add(imagePath);
                            realUri.add(Uri.parse(imagePath));
                            Log.i("tempUri",tempUri.toString());
                            Log.i("imagePath",imagePath);

                            mBitmap.add(BitmapFactory.decodeFile(imagePath));
                            imageView1.setImageBitmap(mBitmap.get(0));

                            f1 = new File(imagePath);
                            textView2.setText("\n1.path: "+imagePath+"\n"
                                    +"2.name: "+f1.getName()+"\n"
                                    +"3.size: "+f1.length()+"\n"
                                    +"4.mimeType: "+getContentResolver().getType(tempUri));
                            Log.i("1","ok");

                        } else if(clipData.getItemCount() > 1 && clipData.getItemCount() <= 5){
                            for(int i=0; i<clipData.getItemCount(); i++){
                                tempUri = clipData.getItemAt(i).getUri();
                                Log.i("single choice", i+" : "+tempUri.toString());
                                imagePath = getPathFromURI(tempUri);
                                imageListUri.add(imagePath);
                                realUri.add(Uri.parse(imagePath));
//                                Cursor cursor = getContentResolver().query(tempUri, filePathColumn, null, null, null);
//                                if (cursor == null)
//                                    Log.e("TAG", "cursor is null");
//                                else {
//                                    if (cursor.moveToFirst()) {
//                                        int columnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
//                                        imagePath = cursor.getString(columnIndex);
//                                        if(imagePath == null)
//                                            Log.e("TAG", "videoPath is null");
//                                        imageListUri.add(imagePath);
//                                        realUri.add(i, Uri.parse(imagePath));
//                                    } else {
//                                        Log.e("TAG", "cannot use cursor");
//                                    }
//                                    cursor.close();
//                                }

//                                imageListUri.add(i, imagePath);
//                                realUri.add(i, Uri.parse(imagePath));
                                Log.i("single choice", i+" : "+realUri.get(i));
//                                mBitmap.add(MediaStore.Images.Media.getBitmap(getContentResolver(),realUri.get(0)));
                                mBitmap.add(BitmapFactory.decodeFile(imagePath));

                                f1 = new File(imagePath);
                                textView2.setText(textView2.getText().toString()
                                        +"\n1.path: "+imagePath+"\n"
                                        +"2.name: "+f1.getName()+"\n"
                                        +"3.size: "+f1.length()+"\n"
                                        +"4.mimeType: "+getContentResolver().getType(tempUri)+"\n");
                            }
                            if(clipData.getItemCount() == 2) {
                                imageView1.setImageBitmap(mBitmap.get(0));
                                imageView2.setImageBitmap(mBitmap.get(1));

                            } if (clipData.getItemCount() == 3) {
                                imageView1.setImageBitmap(mBitmap.get(0));
                                imageView2.setImageBitmap(mBitmap.get(1));
                                imageView3.setImageBitmap(mBitmap.get(2));

                            } if(clipData.getItemCount() == 4) {
                                imageView1.setImageBitmap(mBitmap.get(0));
                                imageView2.setImageBitmap(mBitmap.get(1));
                                imageView3.setImageBitmap(mBitmap.get(2));
                                imageView4.setImageBitmap(mBitmap.get(3));

                            } if(clipData.getItemCount() == 5) {
                                imageView1.setImageBitmap(mBitmap.get(0));
                                imageView2.setImageBitmap(mBitmap.get(1));
                                imageView3.setImageBitmap(mBitmap.get(2));
                                imageView4.setImageBitmap(mBitmap.get(3));
                                imageView5.setImageBitmap(mBitmap.get(4));
                            }

                        }
                    }
                }
            }
        } else{
            Log.d("e","사진 업로드 실패");
        }

    }


    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }

    public String getImageFilePath(Intent data) { return getImageFromFilePath(data); }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

//    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        switch (requestCode) {
//            case ALL_PERMISSIONS_RESULT:
//                for (String perms : permissionsToRequest) {
//                    if (!hasPermission(perms)) {
//                        permissionsRejected.add(perms);
//                    }
//                }
//
//                if (permissionsRejected.size() > 0) {
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
//                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
//                                        }
//                                    });
//                            return;
//                        }
//                    }
//                }
//                break;
//            case PERMISSION_REQUEST_CODE:
//                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(MainActivity.this, "Permission Successful",Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Permission Failed", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case REQUEST_CAMERA:
//                for ( int g : grantResults ) {
//                    if ( g == PackageManager.PERMISSION_DENIED ) {
//                        //권한거부
//                        return;
//                    }
//                }
//                //임시파일 생성
//                File file = createImgCacheFile( );
//                cacheFilePath = file.getAbsolutePath( );
//                //카메라 호출
//                onCamera( REQUEST_CAMERA, file );
//                break;
//
//        }
//    }

    private void multipartImageUpload(int index) {
        try {
            File file = new File(String.valueOf(realUri.get(index)), "image" + ".png");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.get(index).compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


//            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
//            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user_id);
//
//            Call<ResponseBody> req = serviceApi.postImage(body, id);
//            req.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.code() == 200) {
//                        textView.setText("Uploaded Successfully!");
//                        textView.setTextColor(Color.BLUE);
//                    }
//                    Toast.makeText(getBaseContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    textView.setText("Uploaded Failed!");
//                    textView.setTextColor(Color.RED);
//                    Toast.makeText(getBaseContext(), "Request failed", Toast.LENGTH_SHORT).show();
//                    t.printStackTrace();
//                }
//            });

            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user_id);
            ArrayList<MultipartBody.Part> files = new ArrayList<>();
            for (int i =0; i<realUri.size(); ++i) {
                // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성
                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                // RequestBody로 Multipart.Part 객체 생성
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
                // 추가
                files.add(filePart);
            }
            Call<ResponseBody> requestfile = serviceApi.request(files, id);
            requestfile.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        textView.setText("Uploaded Successfully!");
                        textView.setTextColor(Color.BLUE);
                    }
                    Toast.makeText(getBaseContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    textView.setText("Uploaded Failed!");
                    textView.setTextColor(Color.RED);
                    Toast.makeText(getBaseContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

//            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user_id);
////            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), user_email);
//            map.put("id", id);
////            map.put("email", email);
//            File file2 = new File(imageListUri.get(index));
//            InputStream inputStream = null;
//            inputStream = getBaseContext().getContentResolver().openInputStream(realUri.get(index));
//            mBitmap.set(index, BitmapFactory.decodeStream(inputStream));
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            mBitmap.get(index).compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray());
//            MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("postImg", file.getName() ,requestBody);
//            serviceApi.postImages(uploadFile, map).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.code() == 200) {
//                        textView.setText("Uploaded Successfully!");
//                        textView.setTextColor(Color.BLUE);
//                    }
//                    Toast.makeText(getBaseContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    textView.setText("Uploaded Failed!");
//                    textView.setTextColor(Color.RED);
//                    Toast.makeText(getBaseContext(), "Request failed", Toast.LENGTH_SHORT).show();
//                    t.printStackTrace();
//                }
//            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FirebaseUpload(int index) {
        //firebase storage에 업로드하기
        //1. FirebaseStorage을 관리하는 객체 얻어오기
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();

        //2. 업로드할 파일의 node를 참조하는 객체
        //파일 명이 중복되지 않도록 날짜를 이용
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");
        String filename= sdf.format(new Date())+ ".png";//현재 시간으로 파일명 지정 20191023142634
        //원래 확장자는 파일의 실제 확장자를 얻어와서 사용해야함. 그러려면 이미지의 절대 주소를 구해야함.

        StorageReference imgRef= firebaseStorage.getReference("uploads/"+filename);
        //uploads라는 폴더가 없으면 자동 생성

        //참조 객체를 통해 이미지 파일 업로드
        // imgRef.putFile(imgUri);
        //업로드 결과를 받고 싶다면..
        try {
            if (tempUri == null){
                imgRef.putFile(photoUri);
                Log.i("photoUri","upload 성공");
            } else{
                InputStream stream = new FileInputStream(String.valueOf(realUri.get(index)));
                UploadTask uploadTask =imgRef.putStream(stream);
//            UploadTask uploadTask =imgRef.putFile(Uri.parse(imageListUri.get(index)));
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(String.valueOf(index),"upload 성공");
                        Toast.makeText(getBaseContext(), String.valueOf(index)+": success upload", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //업로드한 파일의 경로를 firebaseDB에 저장하면 게시판 같은 앱도 구현할 수 있음.

    }
}
