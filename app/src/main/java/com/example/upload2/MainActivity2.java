package com.example.upload2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
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
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    private final static String BASE_URL = "http://49.50.172.208:8080";
    private final static int MULTIPLE_PERMISSION = 10235;
    private String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

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
        setContentView(R.layout.activity_main2);

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

        activity.setOnClickListener(this);
        camera.setOnClickListener(this);
        select.setOnClickListener(this);
        upload.setOnClickListener(this);

        if (!hasPermissions(getBaseContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity2.this, PERMISSIONS, MULTIPLE_PERMISSION);
        } else {
            /*..권한이 있는경우 실행할 코드....*/
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                finish();
                break;
            case R.id.camera:
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                break;
            case R.id.select:
                filePicker();
                break;
            case R.id.upload:
                if (mBitmap != null){
                    for (int i = 0; i < mBitmap.size(); i++){
                        multipartImageUpload(i);
//                        FirebaseUpload(i);
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void filePicker(){
        Log.d("call","filePicker");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Get File"),REQUEST_CODE);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MULTIPLE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*..권한이 있는경우 실행할 코드....*/
                } else {
                    // 하나라도 거부한다면.
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getBaseContext());
                    alertDialog.setTitle("앱 권한");
                    alertDialog.setMessage("해당 앱의 원할한 기능을 이용하시려면 애플리케이션 정보>권한> 에서 모든 권한을 허용해 주십시오");
                    // 권한설정 클릭시 이벤트 발생
                    alertDialog.setPositiveButton("권한설정",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + getBaseContext().getPackageName()));
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });
                    //취소
                    alertDialog.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
                return;
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

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public Intent getPickImageChooserIntent() {
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        // 디바이스에 설치된 앱 정보 알아내기 - API 30 부터는 안됨
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
//        if(!getImage.exists()) getImage.mkdirs();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
            photoUri = outputFileUri;
        }
        Log.d("outputFileUri",outputFileUri.toString());
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

    private void multipartImageUpload(int index) {
        try {
            String user_id = "abc";
            String user_email = "abc@gmail.com";

//            File filesDir = getApplicationContext().getFilesDir();
//            File file = new File(filesDir, "image" + ".png");

            File file = new File(imageListUri.get(index));
//
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.get(index).compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user_id);

            Call<ResponseBody> req = serviceApi.postImage(body, id);
            req.enqueue(new Callback<ResponseBody>() {
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

//            Log.d("imageListUri", imageListUri.get(index));
//            Log.d("realUri", String.valueOf(realUri.get(index)));
//            Uri returnedUri = Uri.parse("file://"+imageListUri.get(index));
//            InputStream inputStream = null;
//            inputStream = getBaseContext().getContentResolver().openInputStream(realUri.get(index));
//            mBitmap.set(index, BitmapFactory.decodeStream(inputStream));
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            mBitmap.get(index).compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);


//            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user_id);
//            ArrayList<MultipartBody.Part> files = new ArrayList<>();
//            for (int i =0; i<realUri.size(); ++i) {
//                // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성
//                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), byteArrayOutputStream.toByteArray());
////                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                // RequestBody로 Multipart.Part 객체 생성
//                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
//                // 추가
//                files.add(filePart);
//            }
//            Call<ResponseBody> requestfile = serviceApi.request(files, id);
//            requestfile.enqueue(new Callback<ResponseBody>() {
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