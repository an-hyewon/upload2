package com.example.upload2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
=======
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

<<<<<<< HEAD
//    private final static String BASE_URL = "http://49.50.172.208:8080/";
=======
    private final static String BASE_URL = "http://49.50.172.208:8080/";
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
    private final static int MULTIPLE_PERMISSION = 10235;
    private String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };
    String currentPhotoPath;
    String mediaPath;
<<<<<<< HEAD
//    ServiceApi serviceApi;
//    Retrofit retrofit = null;
=======
    ServiceApi serviceApi;
    Retrofit retrofit = null;
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
    Uri picUri, photoUri;
    Uri tempUri = null;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    public final static int REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 10;
    private final static int REQUEST_IMAGE_CAPTURE = 130;

    private static final int REQUEST_CAMERA = 100;
<<<<<<< HEAD
    private String cacheFilePath;
=======
    private String cacheFilePath = null;
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb

    ArrayList<Bitmap> mBitmap = new ArrayList<>();
    Bitmap mBitmap1;
    private Button activity, camera, select, upload;
    private TextView textView, textView2;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;

    String[] filePathColumn = { MediaStore.Video.Media.DATA };
<<<<<<< HEAD
    String imagePath;
=======
    String imagePath = null;
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
    ArrayList<String> imageListUri = new ArrayList<>();
    ArrayList<Uri> realUri = new ArrayList<>();
    File f1;

<<<<<<< HEAD
    public static final int PICK_IMAGE = 100;
    private String filePath;

=======
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
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

<<<<<<< HEAD
        askPermissions();

=======
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
//        if (!hasPermissions(getBaseContext(), PERMISSIONS)) {
//            ActivityCompat.requestPermissions(MainActivity2.this, PERMISSIONS, MULTIPLE_PERMISSION);
//        } else {
//            /*..권한이 있는경우 실행할 코드....*/
//        }
<<<<<<< HEAD

=======
        askPermissions();
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
//        initRetrofitClient();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                finish();
                break;
<<<<<<< HEAD
            case R.id.btn_upload:
                Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
                break;
=======
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
            case R.id.camera:
//                checkPermission();
//                camera_open_intent();
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                break;
            case R.id.select:
//                checkPermission();
                filePicker();
                break;
            case R.id.upload:
                if (mBitmap != null){
<<<<<<< HEAD
                    multipartImageUpload();
=======
                    for (int i = 0; i < mBitmap.size(); i++){
                        multipartImageUpload(i);
//                        FirebaseUpload(i);
                    }
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
                }
                else {
                    Toast.makeText(getBaseContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void camera_open_intent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("Woongs",ex.getMessage().toString());
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri providerURI = FileProvider.getUriForFile( getBaseContext() , "com.example.upload2.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // 파일 생성
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // 참고: getExternalFilesDir() 또는 getFilesDir()에서 제공한 디렉터리에 저장한 파일은 사용자가 앱을 제거할 때 삭제됩니다.

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    // 파일 저장
    private void saveFile(Uri image_uri) {

        ContentValues values = new ContentValues();
        String fileName =  "woongs"+System.currentTimeMillis()+".png";
        values.put(MediaStore.Images.Media.DISPLAY_NAME,fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
        }

        ContentResolver contentResolver = getContentResolver();
        Uri item = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            ParcelFileDescriptor pdf = contentResolver.openFileDescriptor(item, "w", null);
            if (pdf == null) {
                Log.d("Woongs", "null");
            } else {
                byte[] inputData = getBytes(image_uri);
                FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());
                fos.write(inputData);
                fos.close();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    values.clear();
                    values.put(MediaStore.Images.Media.IS_PENDING, 0);
                    contentResolver.update(item, values, null, null);
                }

                // 갱신
                galleryAddPic(fileName);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("Woongs", "FileNotFoundException  : "+e.getLocalizedMessage());
        } catch (Exception e) {
            Log.d("Woongs", "FileOutputStream = : " + e.getMessage());
        }
    }

    // Uri to ByteArr
    public byte[] getBytes(Uri image_uri) throws IOException {
        InputStream iStream = getContentResolver().openInputStream(image_uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024; // 버퍼 크기
        byte[] buffer = new byte[bufferSize]; // 버퍼 배열

        int len = 0;
        // InputStream에서 읽어올 게 없을 때까지 바이트 배열에 쓴다.
        while ((len = iStream.read(buffer)) != -1)
            byteBuffer.write(buffer, 0, len);
        return byteBuffer.toByteArray();
    }

    private void galleryAddPic(String Image_Path) {

        Log.d("Woongs","갱신 : "+Image_Path);

        // 이전 사용 방식
        /*Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(Image_Path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.context.sendBroadcast(mediaScanIntent);*/

        File file = new File(Image_Path);
        MediaScannerConnection.scanFile(getBaseContext(), new String[]{file.toString()}, null, null);
    }

    private void gallery_open_intent(){
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_CODE);
    }

    public void checkPermission(){
        String[] permission_list = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);
            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    Uri picCollection = MediaStore.Images.Media
                            .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                    ContentValues picDetail = new ContentValues();
                    picDetail.put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.getName());
                    picDetail.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                    picDetail.put(MediaStore.Images.Media.RELATIVE_PATH,"DCIM/" + UUID.randomUUID().toString());
                    picDetail.put(MediaStore.Images.Media.IS_PENDING,1);
                    Uri finaluri = resolver.insert(picCollection, picDetail);
                    picDetail.clear();
                    picDetail.put(MediaStore.Images.Media.IS_PENDING, 0);
                    resolver.update(picCollection, picDetail, null, null);
                    return finaluri;
                }else {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return context.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                }

            } else {
                return null;
            }
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
        permissions.add(MANAGE_EXTERNAL_STORAGE);
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

//    private void initRetrofitClient() {
////        OkHttpClient client = new OkHttpClient.Builder().build();
//
//        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        clientBuilder.addInterceptor(loggingInterceptor);
//
//        Log.d("OkHttp","initMyAPI : "+BASE_URL);
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(clientBuilder.build())
//                .build();
//
//        serviceApi = retrofit.create(ServiceApi.class);
//    }

<<<<<<< HEAD
//    public Retrofit getClient() {
//        if (retrofit==null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(createOkHttpClient())
//                    .addConverterFactory(new NullOnEmptyConverterFactory())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//
//    public OkHttpClient createOkHttpClient() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new Interceptor() {
//                    @NotNull
//                    @Override
//                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
//                        Request request = chain.request().newBuilder()
//                                .addHeader("Connection","close")
//                                .build();
//                        return chain.proceed(request);
//                    }
//                })
//                .addInterceptor(interceptor)
//                .build();
//
//        // 네트워크 통신 로그(서버로 보내는 파라미터 및 받는 파라미터) 보기
////        OkHttpClient.Builder builder = new OkHttpClient.Builder()
////                .connectTimeout(100, TimeUnit.SECONDS)
////                .readTimeout(100, TimeUnit.SECONDS)
////                .writeTimeout(100, TimeUnit.SECONDS)
////                .retryOnConnectionFailure(true)
////                .addNetworkInterceptor(new Interceptor() {
////                    @NotNull
////                    @Override
////                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
////                        Request request = chain.request().newBuilder()
////                                .addHeader("Connection", "close")
//////                            .addHeader("Transfer-Encoding", "chunked")
////                                .build();
////                        return chain.proceed(request);
////                    }
////                })
////                .addInterceptor(interceptor);
//
////        builder.addInterceptor(interceptor);
//        return client;
//    }
=======
    public Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(createOkHttpClient())
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 네트워크 통신 로그(서버로 보내는 파라미터 및 받는 파라미터) 보기
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Connection", "close")
//                            .addHeader("Transfer-Encoding", "chunked")
                            .build();
                    return chain.proceed(request);
                });

//        builder.addInterceptor(interceptor);
        return builder.build();
    }
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb

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
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "image.png"));
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
            } else if (requestCode == REQUEST_IMAGE_CAPTURE){
                Uri test = Uri.fromFile(new File(currentPhotoPath));
//                saveFile(test);

                imagePath = test.getPath();
                imageListUri.add(imagePath);
                realUri.add(test);
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
                            +"4.mimeType: "+getContentResolver().getType(test)+"\n");
                }
<<<<<<< HEAD
            } else if(requestCode == PICK_IMAGE){
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                cursor.moveToFirst();
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                if (cursor == null){
//                    return;
//                }

//                cursor.moveToFirst();

//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(column_index);
                cursor.close();

//                File file = new File(filePath);
//
//                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
//                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
//                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
//
////            Log.d("THIS", data.getData().getPath());
//
//                retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, name);
//                req.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        t.printStackTrace();
//                    }
//                });

        }} else{
=======
            }
        } else{
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
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
//        String[] projection = new String[]{
//                MediaStore.Images.ImageColumns._ID,
//                MediaStore.Images.ImageColumns.DATA,
//                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
//                MediaStore.Images.ImageColumns.DATE_TAKEN,
//                MediaStore.Images.ImageColumns.MIME_TYPE,
//                MediaStore.Images.ImageColumns.DISPLAY_NAME,
//        };
//        Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


<<<<<<< HEAD
    private void multipartImageUpload() {

        try {
//            serviceApi = getClient().create(ServiceApi.class);

            Retrofit retrofit = RetrofitClient.getClient();
            ServiceApi serviceApi = retrofit.create(ServiceApi.class);
//        Service service = retrofit.create(Service.class);

            List<MultipartBody.Part> files = new ArrayList<>();
            long user_id = 123L;
            for (int i=0;i<realUri.size();i++){
                Log.d("imageListUri", imageListUri.get(i));
                Log.d("realUri", String.valueOf(realUri.get(i)));

                File file = new File(imageListUri.get(i));

//            mediaPath = "/storage/emulated/0/Download/20200711_224908.jpg";
//            File file = new File(mediaPath);
//            mBitmap.add(BitmapFactory.decodeFile(mediaPath));

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mBitmap.get(i).compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("upload", file.getName(), fileBody);
                files.add(filePart);
            }
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user_id));

            Call<ResponseBody> req = serviceApi.imageTest(files, id);
=======
    private void multipartImageUpload(int index) {

        try {
            serviceApi = getClient().create(ServiceApi.class);

            String user_id = "abc";
            String user_email = "abc@gmail.com";

            Log.d("imageListUri", imageListUri.get(index));
            Log.d("realUri", String.valueOf(realUri.get(index)));

//            File filesDir = getApplicationContext().getFilesDir();
//            File file = new File(filesDir, "image" + ".png");

            File file = new File(imageListUri.get(index));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.get(index).compress(Bitmap.CompressFormat.PNG, 50, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user_id);


            Call<ResponseBody> req = serviceApi.postImage(body, id);
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        textView.setText("postImage Success");
                        textView.setTextColor(Color.BLUE);
                    }
                    Toast.makeText(getBaseContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    textView.setText("postImage Failed");
                    textView.setTextColor(Color.RED);
                    Toast.makeText(getBaseContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });


<<<<<<< HEAD

//            File filesDir = getApplicationContext().getFilesDir();
//            File file = new File(filesDir, "image" + ".png");

//            File file = new File(imageListUri.get(index));
//
//            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
//            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
//
////            Log.d("THIS", data.getData().getPath());
//
//            retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, name);
//            req.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.code() == 200) {
//                        textView.setText("postImage Success");
//                        textView.setTextColor(Color.BLUE);
//                    }
//                    Toast.makeText(getBaseContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    textView.setText("postImage Failed");
//                    textView.setTextColor(Color.RED);
//                    Toast.makeText(getBaseContext(), "Request failed", Toast.LENGTH_SHORT).show();
//                    t.printStackTrace();
//                }
//            });

//            File file = new File(filePath);
////            File file = new File(imageListUri.get(index));
//
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            mBitmap.get(index).compress(Bitmap.CompressFormat.PNG, 50, bos);
//            byte[] bitmapdata = bos.toByteArray();
//
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(bitmapdata);
//            fos.flush();
//            fos.close();


//            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
//            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user_id);


//            Call<ResponseBody> req = serviceApi.postImage(body, id);
//            req.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.code() == 200) {
//                        textView.setText("postImage Success");
//                        textView.setTextColor(Color.BLUE);
//                    }
//                    Toast.makeText(getBaseContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    textView.setText("postImage Failed");
//                    textView.setTextColor(Color.RED);
//                    Toast.makeText(getBaseContext(), "Request failed", Toast.LENGTH_SHORT).show();
//                    t.printStackTrace();
//                }
//            });


=======
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
//            Uri returnedUri = Uri.parse("file://"+imageListUri.get(index));
//            InputStream inputStream = null;
//            inputStream = getBaseContext().getContentResolver().openInputStream(realUri.get(index));
//            mBitmap.set(index, BitmapFactory.decodeStream(inputStream));
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            mBitmap.get(index).compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
//
//
//            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user_id);
//            ArrayList<MultipartBody.Part> files = new ArrayList<>();
//            for (int i =0; i<realUri.size(); ++i) {
//                // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성
////                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), byteArrayOutputStream.toByteArray());
//                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
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
//
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


        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
//    public void FirebaseUpload(int index) {
//        //firebase storage에 업로드하기
//        //1. FirebaseStorage을 관리하는 객체 얻어오기
//        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
//
//        //2. 업로드할 파일의 node를 참조하는 객체
//        //파일 명이 중복되지 않도록 날짜를 이용
//        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");
//        String filename= sdf.format(new Date())+ ".png";//현재 시간으로 파일명 지정 20191023142634
//        //원래 확장자는 파일의 실제 확장자를 얻어와서 사용해야함. 그러려면 이미지의 절대 주소를 구해야함.
//
//        StorageReference imgRef= firebaseStorage.getReference("uploads/"+filename);
//        //uploads라는 폴더가 없으면 자동 생성
//
//        //참조 객체를 통해 이미지 파일 업로드
//        // imgRef.putFile(imgUri);
//        //업로드 결과를 받고 싶다면..
//        try {
//            if (tempUri == null){
//                imgRef.putFile(photoUri);
//                Log.i("photoUri","upload 성공");
//            } else{
//                InputStream stream = new FileInputStream(String.valueOf(realUri.get(index)));
//                UploadTask uploadTask =imgRef.putStream(stream);
////            UploadTask uploadTask =imgRef.putFile(Uri.parse(imageListUri.get(index)));
//                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Log.i(String.valueOf(index),"upload 성공");
//                        Toast.makeText(getBaseContext(), String.valueOf(index)+": success upload", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        //업로드한 파일의 경로를 firebaseDB에 저장하면 게시판 같은 앱도 구현할 수 있음.
//
//    }
//
//    private void patchEOFException() {
//        System.setProperty("http.keepAlive", "false");
//    }
=======
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

    private void patchEOFException() {
        System.setProperty("http.keepAlive", "false");
    }
>>>>>>> bf53af2d47e29451d53397e5e0cea36bbab786eb
}