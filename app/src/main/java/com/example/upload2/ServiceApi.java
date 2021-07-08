package com.example.upload2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ServiceApi {
    // 넘기는 값 user_id, multipart file
    @Multipart
    @POST("/users/image/new")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part RequestBody id);

    @Multipart
    @POST("/users/image/new")
    Call<ResponseBody> postImages(@Part MultipartBody.Part postImg, @PartMap HashMap<String, RequestBody> data);

    @Multipart
    @POST("/users/image/new")
    Call<ResponseBody> request(@Part ArrayList<MultipartBody.Part> files, @Part RequestBody id);
}
