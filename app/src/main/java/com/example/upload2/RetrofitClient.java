package com.example.upload2;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL = "http://49.50.172.208:8080";
//    private final static String BASE_URL = "http://192.168.0.234:3000";
    private static ServiceApi serviceApi;
    private static Service service;
    private static RetrofitClient instance = null;
    private static Retrofit retrofit = null;

    private RetrofitClient() {
    }

    public static Retrofit getClient() {
            //로그를 보기 위한 Interceptor
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                            Request request = chain.request().newBuilder()
//                                    .addHeader("Content-Transfer-Encoding","chunked")
                                    .addHeader("Connection","close")
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .retryOnConnectionFailure(true)
                    .build();

        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client) //로그 기능 추가
                    .build();
        }
        return retrofit;
    }

}
