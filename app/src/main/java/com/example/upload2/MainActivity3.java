package com.example.upload2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.upload2.model.ApiModel;
import com.example.upload2.network.ApiConstants;
import com.example.upload2.network.ServiceInterface;
import com.example.upload2.utils.FileUtil;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity3 extends AppCompatActivity {
    private static final int TAKE_IMAGE = 234;
    ImageView selectedImage;
    Button btnSubmit;
    ServiceApi serviceApi;
    List<Uri> urifiles = new ArrayList<>();

    private Uri cameraUri;

    private LinearLayout parentLinearLayout;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        parentLinearLayout= findViewById(R.id.parent_linear_layout);

        ImageView addImage = findViewById(R.id.iv_add_image);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        btnSubmit = findViewById(R.id.btnupload);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                uploadImages();
            }
        });

    }


    //===== add image in layout
    public void addImage() {
        LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView=inflater.inflate(R.layout.image, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        parentLinearLayout.isFocusable();

        selectedImage = rowView.findViewById(R.id.number_edit_text);
        selectImage(MainActivity3.this);
    }

    //===== select image
    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Choose a Media");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            private ContentResolver contentResolver;

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
//                    if (//hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) 카메라 있으면
//                            true
//                    ){//버전별로 분기처리
//                        if (Build.VERSION.SDK_INT >= 29) {
//
//                        } else if (Build.VERSION.SDK_INT<29 && Build.VERSION.SDK_INT >=23){
//
//                        } else {// 22 이하
//
//                        }
//
//                    } else{//카메라 없으면 종료
//
//                    }



                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {

                        Bitmap img = (Bitmap) data.getExtras().get("data");
                        selectedImage.setImageBitmap(img);
                        Picasso.get().load(getImageUri(MainActivity3.this,img)).into(selectedImage);

                        String imgPath = FileUtil.getPath(MainActivity3.this,getImageUri(MainActivity3.this,img));

                        urifiles.add(Uri.parse(imgPath));
                        Log.e("image", imgPath);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri img = data.getData();
                        Picasso.get().load(img).into(selectedImage);

                        String imgPath = FileUtil.getPath(MainActivity3.this,img);

                        urifiles.add(Uri.parse(imgPath));
                        Log.e("image", imgPath);

                    }
                    break;
            }
        }
    }

    //===== bitmap to Uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "intuenty", null);
        Log.d("image uri",path);
        return Uri.parse(path);
    }

    //===== Upload files to server
    public void uploadImages(){
        List<MultipartBody.Part> files = new ArrayList<>();

        for (Uri uri:urifiles) {
            Log.i("uris",uri.getPath());

//            File file = new File(uri.getPath());
//
//            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            MultipartBody.Part filePart = MultipartBody.Part.createFormData("upload", file.getName(), fileBody);
//            files.add(filePart);

            files.add(prepareFilePart("upload", uri));
        }

        serviceApi = ApiConstants.getClient().create(ServiceApi.class);

        long user_id = 123L;
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user_id));



        Call<ResponseBody> req = serviceApi.imageTest(files, id);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("result", "Success");
                }
                Toast.makeText(getBaseContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("result", "Fail");
                Toast.makeText(getBaseContext(), "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = new File(fileUri.getPath());
        Log.i("here is error",file.getAbsolutePath());
        // create RequestBody instance from file

//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);


    }


    // this is all you need to grant your application external storage permision
    private void requestPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity3.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    uploadImages();
                }
                else {
                    Toast.makeText(MainActivity3.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Nullable
    public final Uri getUriForAndroidQ(@NotNull ContentResolver contentResolver) {
        Intrinsics.checkNotNullParameter(contentResolver, "contentResolver");
        String folderName = "Choding";
        String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
        Intrinsics.checkNotNullExpressionValue(timeStamp, "SimpleDateFormat(\"yyyyMMdd_HHmmss\").format(Date())");
        String picturePath = Environment.DIRECTORY_PICTURES + File.separator + folderName;
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", timeStamp + ".jpg");
        contentValues.put("mime_type", "image/jpg");
        contentValues.put("relative_path", picturePath);
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

}