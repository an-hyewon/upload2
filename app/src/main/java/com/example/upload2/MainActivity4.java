package com.example.upload2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upload2.utils.FileUtil2;
import com.example.upload2.utils.FileUtil3;

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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity4 extends AppCompatActivity {
    Button btnCamera, btnSelect, btnUpload;
    TextView textView;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    private int PICK_IMAGE_MULTIPLE = 1;
    private int PICK_IMAGE_SAMSUNG = 2;

    private ArrayList<Bitmap> mBitmap = new ArrayList<>();
    private ArrayList<String> imageListUri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        btnCamera = findViewById(R.id.button5);
        btnSelect = findViewById(R.id.button6);
        btnUpload = findViewById(R.id.button7);
        textView = findViewById(R.id.textView3);

        askPermissions();

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageMultiPick();
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multipartImageUpload();
            }
        });
    }

    public void imageMultiPick() {
        // Undocumented way to get multiple photo selections from Android Gallery ( on Samsung )
        Intent intent = new Intent("android.intent.action.MULTIPLE_PICK");//("Intent.ACTION_GET_CONTENT);
        // intent.addCategory(Intent.CATEGORY_OPENABLE); // ???????????? ??? - ?????? ?????????S3 ?????????
        intent.setType("image/*"); // ???????????? ?????? ?????? ????????? > else { ... ???????????? ?????????
        // Check to see if it can be handled...
        PackageManager manager = getApplicationContext().getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);

        if (infos.size() > 0) {
            Log.e("FAT=","?????????");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // ????????? - ???????????? ???
            // createChooser ???????????? Intent??? 1??? ????????? ?????? > ????????? ???????????? ?????? ??????
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_SAMSUNG);
        }
        else {
            Log.e("FAT=","?????????");
            // intent.addCategory(Intent.CATEGORY_OPENABLE); // ????????? ??? - ?????? G2 ?????????
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // ????????? - ????????? ????????? ???????????? ??????
            intent.setAction(Intent.ACTION_PICK); // ACTION_GET_CONTENT ???????????? - ?????? G2 ?????????
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // ?????? : http://coder-jeff.blogspot.kr/2016/05/how-to-pick-multiple-files-from.html
        // ?????? : NullPointerException ?????? - (1) ???????????? (2) ????????? ??????
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        // if (data.getAction().equals("android.intent.action.MULTIPLE_PICK")) { // NullPointerException
        if (requestCode == PICK_IMAGE_SAMSUNG) {
            final Bundle extras = data.getExtras();
            int count = extras.getInt("selectedCount");
            Object items = extras.getStringArrayList("selectedItems");
            // do somthing
            Log.e("FAT=", "????????? : "+items.toString());
        }
        else {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                // do somthing
                Log.e("FAT=", "?????????/?????? : "+uri.toString());
                String path = FileUtil3.getPath(this, uri);
                Log.e("path",path);
                imageListUri.add(path);
                mBitmap.add(BitmapFactory.decodeFile(path));

            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        ArrayList<Uri> uris = new ArrayList<>();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();
                            Log.e("FAT=", "?????????/?????? : "+uri.toString());
                            String path = FileUtil3.getPath(this, uri);
                            Log.e("path",path);
                            imageListUri.add(path);
                            mBitmap.add(BitmapFactory.decodeFile(path));

                        }
                        // Do someting
                    }
                } else {// sdk 29,30

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void multipartImageUpload() {

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
//                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
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
}